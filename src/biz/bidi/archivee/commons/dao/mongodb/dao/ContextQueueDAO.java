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
package biz.bidi.archivee.commons.dao.mongodb.dao;

import com.google.code.morphia.query.Query;

import biz.bidi.archivee.commons.ArchiveeConstants;
import biz.bidi.archivee.commons.dao.mongodb.ArchiveeMongodbDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.mongodb.ContextQueue;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 28, 2012
 */
public class ContextQueueDAO extends ArchiveeMongodbDAO<ContextQueue> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.dao.IArchiveeGenericDAO#find(java.lang.Object, java.lang.String)
	 */
	@Override
	public Query<ContextQueue> find(ContextQueue entity, String customSearchId) throws ArchiveeException {
		
		if(customSearchId.equals(ArchiveeConstants.CONTEXT_QUEUE_KEY_QUERY)){
			return find(entity).
			field("key.patternId").equal(entity.getKey().getPatternId()).
			field("key.sequence").equal(entity.getKey().getSequence()).
			field("key.isAtQueue").equal(entity.getKey().isAtQueue());
		}
		
		if(customSearchId.equals(ArchiveeConstants.CONTEXT_QUEUE_NEXT_KEY_QUERY)){
			return find(entity).
			field("key.patternId").equal(entity.getKey().getPatternId()).
			field("key.isAtQueue").equal(entity.getKey().isAtQueue()).
			order("-key.sequence");
		}
		
		return null;
	}

}
