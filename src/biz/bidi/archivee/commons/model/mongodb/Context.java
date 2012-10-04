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
package biz.bidi.archivee.commons.model.mongodb;

import java.util.Date;

import org.bson.types.ObjectId;

import biz.bidi.archivee.commons.ArchiveeConstants;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 27, 2012
 */
@Entity(value="context", noClassnameStored=true)
public class Context implements IEntity {

	@Id
	private ObjectId id;
	
	@Indexed(unique=true)
	private ContextKey key;
	
	@Indexed
	private Date startDate;
	
	/**
	 * The compacted data
	 */
	private Byte[] data;

	/**
	 * 
	 */
	public Context() {
		super();
		
		data = new Byte[ArchiveeConstants.CONTEXT_MAX_DATA_SIZE];
	}

	/**
	 * @return the data
	 */
	public Byte[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Byte[] data) {
		this.data = data;
	}

	/**
	 * @return the id
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(ObjectId id) {
		this.id = id;
	}

	/**
	 * @return the key
	 */
	public ContextKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(ContextKey key) {
		this.key = key;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}