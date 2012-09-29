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

import java.util.HashMap;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 27, 2012
 */
@Entity(value="dictionary_queue", noClassnameStored=true)
public class DictionaryQueue implements IEntity {

	@Id
	private ObjectId id;
	
	@Indexed(unique=true)
	private DictionaryKey key;
	
	private HashMap<String, Long> counts;

	/**
	 * 
	 */
	private DictionaryQueue() {
		super();
		counts = new HashMap<String, Long>();
	}

	/**
	 * @return the key
	 */
	public DictionaryKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(DictionaryKey key) {
		this.key = key;
	}

	/**
	 * @return the counts
	 */
	public HashMap<String, Long> getCounts() {
		return counts;
	}

	/**
	 * @param counts the counts to set
	 */
	public void setCounts(HashMap<String, Long> counts) {
		this.counts = counts;
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