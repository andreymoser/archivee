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
package biz.bidi.archivee.components.archiver;

import java.util.ArrayList;
import java.util.Date;

import biz.bidi.archivee.commons.components.ArchiveeComponent;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.mongodb.ContextQueue;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.commons.model.mongodb.PatternPath;
import biz.bidi.archivee.commons.model.xml.PatternMessage;
import biz.bidi.archivee.commons.utils.ArchiveeDateUtils;
import biz.bidi.archivee.commons.utils.ArchiveePatternUtils;
import biz.bidi.archivee.components.archiver.commons.ArchiverUtils;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 29, 2012
 */
public class Archiver extends ArchiveeComponent implements IArchiver {

	public Archiver() {
		try {
			patternDAO = ArchiverUtils.getPatternDAO();
			templateDAO = ArchiverUtils.getTemplate();
			dictionaryQueueDAO = ArchiverUtils.getDictionaryQueue();
			contextQueueDAO = ArchiverUtils.getContextQueue();
		} catch (ArchiveeException e) {
			ArchiveeException.log(e, "Error in init Archiver component.", this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.components.archiver.IArchiver#archiveData(biz.bidi.archivee.commons.model.xml.PatternMessage)
	 */
	@Override
	public void archiveData(PatternMessage message) throws ArchiveeException {
		if(message.getAppId() == null || message.getDate() == null || message.getLevel() == null
				|| 	message.getLevel().isEmpty() || message.getMessage() == null || message.getPatternId() == null) {
			throw new ArchiveeException("Invalid message received in archiver, discarding message.",this,message);
		}
		
		Date date = ArchiveeDateUtils.convertToDate(message.getDate());
		
		//Finds the pattern
		Pattern pattern = new Pattern();
		pattern.setId(message.getPatternId());
		for(Pattern p : patternDAO.find(pattern)) {
			pattern = p;
			break;
		}
		if(pattern.getValue() == null || pattern.getValue().isEmpty()) {
			throw new ArchiveeException("Invalid pattern found for message received in archiver, discarding message.",this,message,pattern);
		}
		
		PatternPath patternPath = ArchiveePatternUtils.findPatternPath(message.getMessage(), pattern);
		
		ArrayList<String> words = ArchiveePatternUtils.getPatternValues(message.getMessage());
		
		saveDictionaryData(message, patternPath, words, pattern);
		
		
		//TODO - WIP
		ContextQueue contextQueue = new ContextQueue();
		contextQueue.setPatternId(message.getPatternId());
		
		for(ContextQueue cq : contextQueueDAO.find(contextQueue)) {
			
		}
		
	}

	/**
	 * @param message
	 * @param patternPath
	 * @param words
	 * @param pattern
	 */
	private void saveDictionaryData(PatternMessage message, PatternPath patternPath, ArrayList<String> words, Pattern pattern) {
	
		
		
		
	}
	
}
