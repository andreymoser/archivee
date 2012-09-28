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

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 13, 2012
 */
@Entity(value="pattern", noClassnameStored=true)
public class Pattern implements IEntity, IPattern {

	@Id
	private long id;
	
	@Indexed(unique=true)
	private PatternKey key;
	
	private int offset;
	
	private ArrayList<PatternChild> patterns;
	
	public Pattern() {
		super();
		
		this.key = new PatternKey();
		this.patterns = new ArrayList<PatternChild>(); 
	}

	/**
	 * @return the id
	 */
	public String getValue() {
		return this.key.getValue();
	}

	/**
	 * @param value the id to set
	 */
	public void setValue(String value) {
		this.key.setValue(value);
	}
	
	/**
	 * @return the appId
	 */
	public int getAppId() {
		return this.key.getAppId();
	}
	
	/**
	 * @param value the appId to set
	 */
	public void setAppId(int appId) {
		this.key.setAppId(appId);
	}

	/**
	 * @return the patterns
	 */
	public ArrayList<PatternChild> getPatterns() {
		return patterns;
	}

	/**
	 * @param patterns the patterns to set
	 */
	public void setPatterns(ArrayList<PatternChild> patterns) {
		this.patterns = patterns;
	}

	public PatternChild findPattern(String id) {
		PatternChild pattern = null;
		
		for(PatternChild p : patterns) {
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
		String fullPattern = this.getValue();
		
		int size = this.getPatterns()!=null?this.getPatterns().size():-1;
		stringBuffer.append("\"" + this.getValue() + "\"" + " [offset: " + this.getOffset() + " elements: " + size + "] " + ((this.getPatterns().size()==0)?(" \""+fullPattern+"\""):""));
		
		if(this.getPatterns() != null) {
			for(PatternChild p : this.getPatterns()) {
				stringBuffer.append("\n");
				getPatternTreeDataString(p,stringBuffer,"+\t", fullPattern + p.getValue());
			}
		}
		
		return stringBuffer;
	}

	/**
	 * @param pattern
	 * @return
	 */
	private void getPatternTreeDataString(PatternChild pattern, StringBuffer stringBuffer, String tabs, String fullPattern) {
		int size = pattern.getPatterns()!=null?pattern.getPatterns().size():-1;
		stringBuffer.append(tabs + "\"" + pattern.getValue() + "\"" + " [offset: " + pattern.getOffset() + " elements: " + size + "] " + ((pattern.getPatterns().size()==0)?(" \""+fullPattern+"\""):""));
		
		if(pattern.getPatterns() != null) {
			for(PatternChild p : pattern.getPatterns()) {
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

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}


}