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
package biz.bidi.archivee.commons.model.xml;

import org.bson.types.ObjectId;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.utils.ArchiveeDateUtils;

/**
 * Message sent from listener to log parser
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 28, 2012
 */
public class PatternMessage implements IXmlObject, Comparable<PatternMessage> {
	
	/**
	 * The pattern id
	 */
	private ObjectId patternId;
	/**
	 * The application id
	 */
	private ObjectId appId
	;
	/**
	 * The date of log line
	 */
	private String date;
	/**
	 * The level of the log
	 */
	private String level;
	/**
	 * The message log content
	 */
	private String message;
	/**
	 * The thread id
	 */
	private long threadId;

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
	 * @return the appId
	 */
	public ObjectId getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(ObjectId appId) {
		this.appId = appId;
	}

	/**
	 * @return the threadId
	 */
	public long getThreadId() {
		return threadId;
	}

	/**
	 * @param threadId the threadId to set
	 */
	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appId == null) ? 0 : appId.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result
				+ ((patternId == null) ? 0 : patternId.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PatternMessage other = (PatternMessage) obj;
		if (appId == null) {
			if (other.appId != null)
				return false;
		} else if (!appId.equals(other.appId))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (patternId == null) {
			if (other.patternId != null)
				return false;
		} else if (!patternId.equals(other.patternId))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PatternMessage o) {
		try {
			int compare = ArchiveeDateUtils.convertToDate(this.getDate()).compareTo(ArchiveeDateUtils.convertToDate(o.getDate()));

			if(compare == 0) {
				if(this.hashCode() > o.hashCode()) {
					compare = -1;
				} else if(this.hashCode() == o.hashCode()) {
					if(!this.equals(o)) {
						compare = -1;
					}
				} else {
					compare = 1;
				}
			}
			
			return compare;
		} catch (ArchiveeException e) {
			ArchiveeException.error(e, "Error in comparator - PatternMessage", this, o);
		}
		return 0;
	}

}
