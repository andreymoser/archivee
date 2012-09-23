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

import org.bson.types.ObjectId;


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

	@Id
	private ObjectId id;
	
	@Indexed 
	private String value;
	
	@Indexed
	private PatternType patternType;
	
	@Indexed
	private int offset;
	
	private ArrayList<Pattern> patterns;
	
	private String parentId;
	
	public Pattern() {
		super();
		
		this.patterns = new ArrayList<Pattern>(); 
	}

	/**
	 * @return the id
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the id to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the patterns
	 */
	public ArrayList<Pattern> getPatterns() {
		return patterns;
	}

	/**
	 * @param patterns the patterns to set
	 */
	public void setPatterns(ArrayList<Pattern> patterns) {
		this.patterns = patterns;
	}

	/**
	 * @return the patternType
	 */
	public PatternType getPatternType() {
		return patternType;
	}

	/**
	 * @param patternType the patternType to set
	 */
	public void setPatternType(PatternType patternType) {
		this.patternType = patternType;
	}

	/**
	 * @return the patternId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the patternId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public boolean isRoot() {
		return patternType.equals(PatternType.root());
	}

	public boolean isDate() {
		return patternType.equals(PatternType.date());
	}

	public boolean isLevel() {
		return patternType.equals(PatternType.level());
	}

	public boolean isPattern() {
		return patternType.equals(PatternType.pattern());
	}

	public Pattern findPattern(String id) {
		Pattern pattern = null;
		
		for(Pattern p : patterns) {
			if(p.getValue().equals(id)) {
				pattern = p;
				break;
			}
		}
		
		return pattern;
	}
	
	/**
	 * Debugging purposes
	 * @return
	 */
	public StringBuffer getTreeStringData() {
		StringBuffer stringBuffer = new StringBuffer();
		getPatternTreeDataString(this,stringBuffer,"",this.getValue());
		return stringBuffer;
	}

	/**
	 * @param pattern
	 * @return
	 */
	private void getPatternTreeDataString(Pattern pattern, StringBuffer stringBuffer, String tabs, String fullPattern) {
		int size = pattern.getPatterns()!=null?pattern.getPatterns().size():-1;
		stringBuffer.append(tabs + "\"" + pattern.getValue() + "\"" + " [offset: " + pattern.getOffset() + " elements: " + size + "] " + ((pattern.getPatterns().size()==0)?(" \""+fullPattern+"\""):""));
		
		if(pattern.getPatterns() != null) {
			for(Pattern p : pattern.getPatterns()) {
				stringBuffer.append("\n");
				getPatternTreeDataString(p,stringBuffer,tabs + "+\t", fullPattern + p.getValue());
			}
		}
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

}