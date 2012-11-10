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
@SuppressWarnings("rawtypes")
@Entity(value="pattern", noClassnameStored=true)
public class Pattern implements IEntity, IPattern {

	@Id
	private ObjectId id;
	
	@Indexed(unique=true)
	private PatternKey key;
	
	private String value;
	
	/**
	 * @deprecated
	 */
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
		return value;
	}

	/**
	 * @param value the id to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @return the appId
	 */
	public ObjectId getAppId() {
		return key.getAppId();
	}
	
	/**
	 * @param value the appId to set
	 */
	public void setAppId(ObjectId appId) {
		key.setAppId(appId);
	}

	/**
	 * @return the threadId
	 */
	public long getThreadId() {
		return key.getThreadId();
	}

	/**
	 * @param threadId the threadId to set
	 */
	public void setThreadId(long threadId) {
		key.setThreadId(threadId);
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
	 * @deprecated
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @deprecated
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
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
	public PatternKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(PatternKey key) {
		this.key = key;
	}

	/**
	 * @param patternPath
	 * @return
	 */
	public String getMessageFormat(PatternPath patternPath) {
		
		String messageFormat = "";
		
		ArrayList<PatternChild> patterns = null;
		
		for(int i=0; i < patternPath.getValues().size(); i++) {
			if(i > 0) {
				PatternPathEntry entry = patternPath.getValues().get(i);
				
				if(patterns == null) {
					break;
				} 
				
				PatternChild pChild = patterns.get(entry.getIndex());
				messageFormat = messageFormat + pChild.getValue();
				
				patterns = pChild.getPatterns();
			} else {
				messageFormat = this.getValue();
				patterns = this.patterns;
			}
		}
		
		return messageFormat;
	}

}