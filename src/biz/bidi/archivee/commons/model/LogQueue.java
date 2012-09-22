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
package biz.bidi.archivee.commons.model;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;


/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 16, 2012
 */
@Entity(value="log_queue", noClassnameStored=true)
public class LogQueue implements IEntity {

	@Id
	private ObjectId id;
	
	@Indexed
	private String line = "";
	
	@Indexed
	private String simpleRegex = "";
	
	/**
	 * @return the line
	 */
	public String getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(String line) {
		this.line = line;
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
	 * @return the simpleRegex
	 */
	public String getSimpleRegex() {
		return simpleRegex;
	}

	/**
	 * @param simpleRegex the simpleRegex to set
	 */
	public void setSimpleRegex(String simpleRegex) {
		this.simpleRegex = simpleRegex;
	}

}
