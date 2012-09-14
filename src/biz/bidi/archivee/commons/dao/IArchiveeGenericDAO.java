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
package biz.bidi.archivee.commons.dao;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 12, 2012
 */
public interface IArchiveeGenericDAO<O,Q> {
	
	/**
	 * Inserts and updates the given entity by id
	 * @param entity
	 * @throws ArchiveeException
	 */
	public void save(O entity) throws ArchiveeException;

	/**
	 * Deletes the given entity by its id
	 * @param entity
	 * @param query
	 * @throws ArchiveeException
	 */
	public void delete(O entity, Q query) throws ArchiveeException;
	
	/**
	 * Return the whole entity data by the given entity id
	 * @param entity
	 * @return
	 * @throws ArchiveeException
	 */
	public O get(O entity) throws ArchiveeException;
	
	/**
	 * Find the entities based on the entity - returns all  
	 * @param customSearchId
	 * @throws ArchiveeException
	 */
	public Q find(O entity) throws ArchiveeException;
	
	/**
	 * Find the entities based on custom searches 
	 * @param customSearchId
	 * @throws ArchiveeException
	 */
	public Q find(O entity, String customSearchId) throws ArchiveeException;
	
	/**
	 * Returns the size of the entire entity  
	 * @param entity
	 * @return
	 * @throws ArchiveeException
	 */
	public long getSize(O entity) throws ArchiveeException;
	
	/**
	 * Returns the size of the query  
	 * @param entity
	 * @return
	 * @throws ArchiveeException
	 */
	public long getQuerySize(Q query) throws ArchiveeException;
}
