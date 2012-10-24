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

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeSet;

import org.bson.types.ObjectId;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.xml.PatternMessage;
import biz.bidi.archivee.commons.utils.ArchiveeDateUtils;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 27, 2012
 */
@Entity(value="context_queue", noClassnameStored=true)
public class ContextQueue implements IEntity {

	@Id
	private ObjectId id;
	
	@Indexed(unique=true)
	private ContextQueueKey key;
	
	private TreeSet<PatternMessage> messages;
	
	private long dataLength;
	
	private Date startDate;
	
	private Date endDate;
	
	private HashMap<ObjectId, Integer> templateCounts;
	
	public ContextQueue() {
		key = new ContextQueueKey();
		
		messages = new TreeSet<PatternMessage>();
		templateCounts = new HashMap<ObjectId, Integer>();
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
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the messages
	 */
	public TreeSet<PatternMessage> getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	public void setMessages(TreeSet<PatternMessage> messages) {
		this.messages = messages;
	}

	/**
	 * @return the key
	 */
	public ContextQueueKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(ContextQueueKey key) {
		this.key = key;
	}

	/**
	 * @return the dataLength
	 */
	public long getDataLength() {
		return dataLength;
	}

	/**
	 * @param dataLength the dataLength to set
	 */
	public void setDataLength(long dataLength) {
		this.dataLength = dataLength;
	}
	
	/**
	 * @return the templateCounts
	 */
	public HashMap<ObjectId, Integer> getTemplateCounts() {
		return templateCounts;
	}

	/**
	 * @param templateCounts the templateCounts to set
	 */
	public void setTemplateCounts(HashMap<ObjectId, Integer> templateCounts) {
		this.templateCounts = templateCounts;
	}

}