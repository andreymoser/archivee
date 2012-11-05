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


/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 27, 2012
 */
public class ContextKey {
	
	/**
	 * @deprecated
	 */
	private ObjectId appId;
	
	private ObjectId patternId;
	
	private long sequence;

	/**
	 * @return the sequence
	 */
	public long getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	/**
	 * @deprecated
	 * @return the appId
	 */
	public ObjectId getAppId() {
		return appId;
	}

	/**
	 * @deprecated
	 * @param appId the appId to set
	 */
	public void setAppId(ObjectId appId) {
		this.appId = appId;
	}

	/**
	 * @return the templateId
	 */
	public ObjectId getPatternId() {
		return patternId;
	}

	/**
	 * @param templateId the templateId to set
	 */
	public void setPatternId(ObjectId templateId) {
		this.patternId = templateId;
	}

}
