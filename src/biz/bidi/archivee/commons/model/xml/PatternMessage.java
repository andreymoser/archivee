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


/**
 * Message sent from listener to log parser
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 28, 2012
 */
public class PatternMessage implements IXmlObject {
	
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


}
