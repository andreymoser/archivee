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

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 27, 2012
 */
@Entity(value="master_index", noClassnameStored=true)
public class MasterIndex implements IEntity {

	@Id
	private String word;
	
	private HashMap<Integer, ArrayList<Long>> patternsByAppId;

	/**
	 * @param word
	 * @param patternsByAppId
	 */
	private MasterIndex() {
		super();
		this.patternsByAppId = new HashMap<Integer, ArrayList<Long>>();
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the patternsByAppId
	 */
	public HashMap<Integer, ArrayList<Long>> getPatternsByAppId() {
		return patternsByAppId;
	}

	/**
	 * @param patternsByAppId the patternsByAppId to set
	 */
	public void setPatternsByAppId(HashMap<Integer, ArrayList<Long>> patternsByAppId) {
		this.patternsByAppId = patternsByAppId;
	}
	
}