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
	
	@Indexed
	private ObjectId patternId;
	
	private TreeSet<PatternMessage> messages;
	
	private Date startDate;
	
	private Date endDate;
	
	public ContextQueue() {
		messages = new TreeSet<PatternMessage>(new Comparator<PatternMessage>() {
			@Override
			public int compare(PatternMessage p1, PatternMessage p2) {
				try {
					return ArchiveeDateUtils.convertToDate(p1.getDate()).compareTo(ArchiveeDateUtils.convertToDate(p2.getDate()));
				} catch (ArchiveeException e) {
					ArchiveeException.log(e, "Error in comparator - ContextQueue", this, p1, p2);
				}
				return 0;
			}
		}); 
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

}