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
import java.util.HashMap;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 27, 2012
 */
@SuppressWarnings("rawtypes")
@Entity(value="master_index", noClassnameStored=true)
public class MasterIndex implements IEntity {

	@Id
	private ObjectId id;
	
	private MasterIndexKey key;
	
	private HashMap<ObjectId, ArrayList<ObjectId>> patternsByAppId;

	/**
	 * @param word
	 * @param patternsByAppId
	 */
	public MasterIndex() {
		super();
		this.key = new MasterIndexKey();
		this.patternsByAppId = new HashMap<ObjectId, ArrayList<ObjectId>>();
	}

	/**
	 * @return the patternsByAppId
	 */
	public HashMap<ObjectId, ArrayList<ObjectId>> getPatternsByAppId() {
		return patternsByAppId;
	}

	/**
	 * @param patternsByAppId the patternsByAppId to set
	 */
	public void setPatternsByAppId(
			HashMap<ObjectId, ArrayList<ObjectId>> patternsByAppId) {
		this.patternsByAppId = patternsByAppId;
	}

	/**
	 * @return the key
	 */
	public MasterIndexKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(MasterIndexKey key) {
		this.key = key;
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