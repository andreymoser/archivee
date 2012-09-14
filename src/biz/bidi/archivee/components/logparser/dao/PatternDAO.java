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
package biz.bidi.archivee.components.logparser.dao;

import biz.bidi.archivee.commons.dao.mongodb.ArchiveeMongodbDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.utils.ArchiveePatternUtils;
import biz.bidi.archivee.components.logparser.model.Pattern;

import com.google.code.morphia.query.Query;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 13, 2012
 */
public class PatternDAO extends ArchiveeMongodbDAO<Pattern> {

	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.dao.IArchiveeGenericDAO#find(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Query find(Pattern entity, String customSearchId) throws ArchiveeException {
		if(customSearchId.equals("all.sorted.by.count.desc")) {
			return ds.find(entity.getClass()).order("-count");
		}
		if(customSearchId.equals("all.starts.with.root")) {
			return ds.find(entity.getClass()).filter("id.parentId","").field("id.id").startsWith(ArchiveePatternUtils.createRegex(entity.getId().getId()));
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.dao.mongodb.ArchiveeMongodbDAO#save(biz.bidi.archivee.commons.model.IEntity)
	 */
	@Override
	public void save(Pattern entity) throws ArchiveeException {
		if(entity.getId().getParentId() == null) {
			entity.getId().setParentId("");
		}
		
		if(entity.getLevel() <= 0) {
			entity.setLevel(0);
		}
		
		if(entity.getId().getParentId().equals("")) {
			entity.setLevel(0);
		}
		
		if(entity.getCount() <= 0) {
			entity.setCount(0);
		}
		
		super.save(entity);
	}

}
