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

import biz.bidi.archivee.commons.exceptions.ArchiveeException;

import com.google.code.morphia.annotations.Transient;


/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 27, 2012
 */
public class TemplateKey {

	private ObjectId patternId;
	
	@Transient
	private PatternPath patternPath;
	
	private String path;
	
	private int sequence;

	public TemplateKey() {
		patternPath = new PatternPath();
	}
	
	/**
	 * @return the patternId
	 */
	public ObjectId getPatternId() {
		return patternId;
	}

	/**
	 * @param patternId the patternId to set
	 */
	public void setPatternId(ObjectId patternId) {
		this.patternId = patternId;
	}

	/**
	 * @return the patternPath
	 */
	public PatternPath getPatternPath() {
		
		this.patternPath = new PatternPath();
		
		for(String values : path.split("|")) {
			PatternPathEntry entry = new PatternPathEntry();
			int i = 0;
			for(String value : values.split(";")) {
				if(i == 0) {
					entry.setIndex(Integer.parseInt(value));
				} else if(i == 1) {
					entry.setWords(Integer.parseInt(value));
				} else {
					ArchiveeException.logError("Warning: Invalid element on path -> TemplateKey.getPatternPath()",path,values,value);
					break;
				}
				i++;
			}
			this.patternPath.getValues().add(entry);
		}
		
		return this.patternPath;
	}

	/**
	 * @param patternPath the patternPath to set
	 */
	public void setPatternPath(PatternPath patternPath) {
		this.patternPath = patternPath;
		
		String path = "";
		int i = 0;
		for(PatternPathEntry entry : patternPath.getValues()) {
			i++;
			path = path + entry.getIndex() + ";" +  entry.getWords() + (i==patternPath.getValues().size()?"":"|");
		}
		
		setPath(path);
	}

	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
