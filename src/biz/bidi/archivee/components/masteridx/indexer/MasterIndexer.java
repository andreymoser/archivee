/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package biz.bidi.archivee.components.masteridx.indexer;

import java.util.ArrayList;

import biz.bidi.archivee.commons.dao.IArchiveeGenericDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.mongodb.MasterIndex;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.commons.model.xml.PatternMessage;
import biz.bidi.archivee.commons.utils.ArchiveePatternUtils;
import biz.bidi.archivee.components.logparser.commons.LogParserUtils;

import com.google.code.morphia.query.Query;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 28, 2012
 */
public class MasterIndexer implements IMasterIndexer {
	
	private IArchiveeGenericDAO<Pattern, Query<Pattern>> patternDAO;
	private IArchiveeGenericDAO<MasterIndex, Query<MasterIndex>> masterIndexDAO;

	public MasterIndexer() {
		try {
			patternDAO = LogParserUtils.getPatternDAO();
			masterIndexDAO = LogParserUtils.getMasterIndex();
		} catch (ArchiveeException e) {
			ArchiveeException.log(e, "Unable to init MasterIndexer sucessfully",this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.components.masteridx.indexer.IMasterIndexer#indexMasterData(biz.bidi.archivee.commons.model.xml.PatternMessage)
	 */
	@Override
	public void indexMasterData(PatternMessage message) throws ArchiveeException {
		if(message.getPatternId() <= 0) {
			throw new ArchiveeException("Invalid message received: pattern id <= 0",message);
		}
		
		Pattern pattern = new Pattern();
		pattern.setId(message.getPatternId());
		
		for(Pattern p : patternDAO.find(pattern)) {
			pattern = p;
			break; // by pattern id is only one possible
		}
		
		for(String word : ArchiveePatternUtils.getPatternValues(message.getMessage())) {
			MasterIndex masterIndex = new MasterIndex();
			masterIndex.setWord(word);
			
			boolean found = false;
			for(MasterIndex idx : masterIndexDAO.find(masterIndex)) {
				updateMasterIndex(idx, pattern);
				masterIndex = idx;
				
				found = true;
				break; //by word is only one
			}
			
			if(!found) {
				updateMasterIndex(masterIndex, pattern);
			}
			
			masterIndexDAO.save(masterIndex);
		}		
	}

	/**
	 * @param idx
	 * @param pattern
	 */
	private void updateMasterIndex(MasterIndex idx, Pattern pattern) {
		if(!idx.getPatternsByAppId().containsKey(pattern.getAppId())) {
			idx.getPatternsByAppId().put(pattern.getAppId(), new ArrayList<Long>());
		}

		ArrayList<Long> patternIds = idx.getPatternsByAppId().get(pattern.getAppId()); 
		if(!patternIds.contains(pattern.getId())) {
			patternIds.add(pattern.getId());
		}
	}

}
