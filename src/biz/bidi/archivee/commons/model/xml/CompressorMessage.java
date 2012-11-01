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

import biz.bidi.archivee.commons.model.xml.enums.CompressorMessageType;


/**
 * Message sent from listener to log parser
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Oct 1, 2012
 */
public class CompressorMessage implements IXmlObject {
	
	/**
	 * The message type
	 */
	private CompressorMessageType messageType;

	/**
	 * The context id
	 */
	private ObjectId contextQueueId;
	/**
	 * The thread id
	 */
	private long threadId;

	/**
	 * @return the contextQueueId
	 */
	public ObjectId getContextQueueId() {
		return contextQueueId;
	}

	/**
	 * @param contextQueueId the contextQueueId to set
	 */
	public void setContextQueueId(ObjectId contextQueueId) {
		this.contextQueueId = contextQueueId;
	}
	
	/**
	 * @return the messageType
	 */
	public CompressorMessageType getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(CompressorMessageType messageType) {
		this.messageType = messageType;
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
}
