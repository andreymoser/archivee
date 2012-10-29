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

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 27, 2012
 */
@Entity(value="locker", noClassnameStored=true)
public class Locker implements IEntity {

	@Id
	private ObjectId id;
	
	@Indexed(unique=true)
	private LockerKey key;
	
	/**
	 * The local thread
	 */
	@Indexed
	private long localThreadId;

	@Indexed
	private boolean isLocked;

	public Locker() {
		key = new LockerKey();
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
	public LockerKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(LockerKey key) {
		this.key = key;
	}

	/**
	 * @return the localThreadId
	 */
	public long getLocalThreadId() {
		return localThreadId;
	}

	/**
	 * @param localThreadId the localThreadId to set
	 */
	public void setLocalThreadId(long localThreadId) {
		this.localThreadId = localThreadId;
	}

	/**
	 * @return the isLocked
	 */
	public boolean isLocked() {
		return isLocked;
	}

	/**
	 * @param isLocked the isLocked to set
	 */
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

}