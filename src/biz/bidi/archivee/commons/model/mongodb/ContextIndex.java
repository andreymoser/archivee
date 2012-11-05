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

import java.util.ArrayList;
import java.util.Date;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 27, 2012
 */
@SuppressWarnings("rawtypes")
@Entity(value="context_index", noClassnameStored=true)
public class ContextIndex implements IEntity {

	@Id
	private ObjectId id;

	@Indexed(unique=true)
	private ContextIndexKey key;
	
	private ArrayList<ObjectId> contextsRef;
	
	@Indexed
	private Date startDate;

	@Indexed
	private Date endDate;

	/**
	 * 
	 */
	public ContextIndex() {
		super();
		
		key = new ContextIndexKey();
		contextsRef = new ArrayList<ObjectId>();
	}

	/**
	 * @return the key
	 */
	public ContextIndexKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(ContextIndexKey key) {
		this.key = key;
	}

	/**
	 * @return the contextsRef
	 */
	public ArrayList<ObjectId> getContextSequences() {
		return contextsRef;
	}

	/**
	 * @param contextsRef the contextsRef to set
	 */
	public void setContextSequences(ArrayList<ObjectId> contextSequences) {
		this.contextsRef = contextSequences;
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

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
}