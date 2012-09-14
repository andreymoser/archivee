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
package biz.bidi.archivee.components.logparser.model;

import java.util.ArrayList;

import biz.bidi.archivee.commons.model.IEntity;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 13, 2012
 */
@Entity(value="pattern", noClassnameStored=true)
public class Pattern implements IEntity {

	@Embedded @Id 
	private PatternId id;
	
	private ArrayList<String> patterns; 
	private ArrayList<Boolean> isElement; 
	
	@Indexed
	private int count;
	
	@Indexed
	private int level;
	
	public Pattern() {
		id = new PatternId();
		id.setParentId("");
	}

	/**
	 * @return the id
	 */
	public PatternId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(PatternId id) {
		this.id = id;
	}

	/**
	 * @return the patterns
	 */
	public ArrayList<String> getPatterns() {
		return patterns;
	}

	/**
	 * @param patterns the patterns to set
	 */
	public void setPatterns(ArrayList<String> patterns) {
		this.patterns = patterns;
	}

	/**
	 * @return the isElement
	 */
	public ArrayList<Boolean> getIsElement() {
		return isElement;
	}

	/**
	 * @param isElement the isElement to set
	 */
	public void setIsElement(ArrayList<Boolean> isElement) {
		this.isElement = isElement;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
}

