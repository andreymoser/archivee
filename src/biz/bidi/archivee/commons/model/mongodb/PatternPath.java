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

import biz.bidi.archivee.commons.exceptions.ArchiveeException;


/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 27, 2012
 */
public class PatternPath {

	private ArrayList<PatternPathEntry> values;
	
	/**
	 * 
	 */
	public PatternPath() {
		super();
		
		values = new ArrayList<PatternPathEntry>();	}

	/**
	 * @return the values
	 */
	public ArrayList<PatternPathEntry> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(ArrayList<PatternPathEntry> values) {
		this.values = values;
	}

	public static PatternPath getPatternPath(String path) {
		PatternPath patternPath = new PatternPath();
		
		for(String values : path.split("|")) {
			PatternPathEntry entry = new PatternPathEntry();
			int i = 0;
			for(String value : values.split(";")) {
				if(i == 0) {
					entry.setIndex(Integer.parseInt(value));
				} else if(i == 1) {
					entry.setWords(Integer.parseInt(value));
				} else {
					ArchiveeException.logError("Warning: Invalid element on path -> PatternPath.getPatternPath()",path,values,value);
					break;
				}
				i++;
			}
			patternPath.getValues().add(entry);
		}
		
		return patternPath;
	}
	
	public static String getPatternPathString(PatternPath patternPath) {
		String path = "";
		
		int i = 0;
		for(PatternPathEntry entry : patternPath.getValues()) {
			i++;
			path = path + entry.getIndex() + ";" +  entry.getWords() + (i==patternPath.getValues().size()?"":"|");
		}
		
		return path;
	}

	public String getPath() {
		return PatternPath.getPatternPathString(this);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return PatternPath.getPatternPathString(this);
	}

	/**
	 * @param words
	 * @param pattern
	 * @return
	 */
	public String mountMessage(ArrayList<String> words, Pattern pattern) {
		String message = pattern.getMessageFormat(this);
		
		for(String word : words) {
			message = message.replaceFirst("W|N|M", word);
		}
		
		return message;
	}
	
}
