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

import biz.bidi.archivee.commons.ArchiveeConstants;
import biz.bidi.archivee.commons.dao.mongodb.ArchiveeMongodbDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.mongodb.LogQueue;
import biz.bidi.archivee.commons.utils.ArchiveePatternUtils;

import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 13, 2012
 */
public class LogQueueDAO extends ArchiveeMongodbDAO<LogQueue> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.dao.IArchiveeGenericDAO#find(java.lang.Object, java.lang.String)
	 */
	@Override
	public Query<LogQueue> find(LogQueue entity, String customSearchId)
			throws ArchiveeException {
		
		if(customSearchId.equals(ArchiveeConstants.LOG_QUEUE_APP_QUERY)) {
			return find(entity).field("appId").equal(entity.getAppId());
		}
		if(customSearchId.equals(ArchiveeConstants.LOG_QUEUE_PATTERN_QUERY)) {
			return find(entity).field("appId").equal(entity.getAppId()).
					field("simpleRegex").startsWith(ArchiveePatternUtils.convertToRegex(entity.getSimpleRegex()));
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.dao.mongodb.ArchiveeMongodbDAO#save(biz.bidi.archivee.commons.model.IEntity)
	 */
	@Override
	public Key<LogQueue> save(LogQueue entity) throws ArchiveeException {
		entity.setSimpleRegex(ArchiveePatternUtils.convertToSimpleRegex(entity.getMessage()));
		
		return super.save(entity);
	}


}
