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

import java.util.HashMap;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Oct 8, 2012
 */
@Entity(value="template_dictionary", noClassnameStored=true)
public class TemplateDictionary implements IEntity {

	@Id
	private ObjectId contextId;

	private HashMap<ObjectId, DictionaryEntry> templateEntries;
	
	private HashMap<Long, ObjectId> templateEntriesIndex;
	
	/**
	 * @return the contextId
	 */
	public ObjectId getContextId() {
		return contextId;
	}

	/**
	 * @param contextId the patternId to set
	 */
	public void setContextId(ObjectId contextId) {
		this.contextId = contextId;
	}

	/**
	 * @return the templateEntries
	 */
	public HashMap<ObjectId, DictionaryEntry> getTemplateEntries() {
		return templateEntries;
	}

	/**
	 * @param templateEntries the templateEntries to set
	 */
	public void setTemplateEntries(HashMap<ObjectId, DictionaryEntry> templateDictionary) {
		this.templateEntries = templateDictionary;
	}

	/**
	 * @return the templateDictionaryIndex
	 */
	public HashMap<Long, ObjectId> getTemplateEntriesIndex() {
		return templateEntriesIndex;
	}

	/**
	 * @param templateDictionaryIndex the templateDictionaryIndex to set
	 */
	public void setTemplateEntriesIndex(HashMap<Long, ObjectId> templateEntriesIndex) {
		this.templateEntriesIndex = templateEntriesIndex;
	}

}