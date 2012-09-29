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
package biz.bidi.archivee.test.sandbox.mongodb;

import biz.bidi.archivee.commons.dao.IArchiveeGenericDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.components.logparser.commons.LogParserUtils;

import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 26, 2012
 */
public class ArchiveeMongoDBTests {
	
	private IArchiveeGenericDAO<Pattern, Query<Pattern>, Key<Pattern>> patternDAO;
	
	public void run() {
		try {
			
			patternDAO = LogParserUtils.getPatternDAO();
			
			Pattern pattern = new Pattern();
			patternDAO.save(pattern);
			
			pattern.setValue("teste");
			patternDAO.save(pattern);
			
			pattern.setValue("teste 2");
			patternDAO.save(pattern);
			
			for(Pattern p : patternDAO.find(new Pattern())) {
//				patternDAO.delete(p, null);
			}
			
			
		} catch (ArchiveeException e) {
			ArchiveeException.log(e, "Generic error", this);
		}
	}
	
	public static void main(String[] args) {
		new ArchiveeMongoDBTests().run();  
	}
}
