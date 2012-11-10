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
package biz.bidi.archivee.components.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeSet;

import javax.faces.bean.ManagedBean;

import org.bson.types.ObjectId;

import biz.bidi.archivee.commons.ArchiveeConstants;
import biz.bidi.archivee.commons.components.ArchiveeManagedComponent;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.interfaces.ISearch;
import biz.bidi.archivee.commons.model.mongodb.Context;
import biz.bidi.archivee.commons.model.mongodb.ContextIndex;
import biz.bidi.archivee.commons.model.mongodb.Dictionary;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.commons.model.mongodb.PatternPath;
import biz.bidi.archivee.commons.model.mongodb.PatternPathEntry;
import biz.bidi.archivee.commons.model.mongodb.Template;
import biz.bidi.archivee.commons.model.mongodb.TemplateDictionary;
import biz.bidi.archivee.commons.utils.ArchiveeByteUtils;
import biz.bidi.archivee.commons.utils.ArchiveeLogger;
import biz.bidi.archivee.components.search.commons.SearchManager;
import biz.bidi.archivee.components.search.model.Log;
import biz.bidi.archivee.components.search.model.LogQueryResponse;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Nov 5, 2012
 */
@ManagedBean(name="search")
public class Search extends ArchiveeManagedComponent implements ISearch {
	
	private String value;
	
	private Log[] logs = null;
	/**
	 * The level values
	 */
	protected ArrayList<String> levels; 

	public Search() {
		try {
			contextIndexDAO = SearchManager.getInstance().getContextIndexDAO();
			contextDAO = SearchManager.getInstance().getContextDAO();
			templateDictionaryDAO = SearchManager.getInstance().getTemplateDictionaryDAO();
			templateDAO = SearchManager.getInstance().getTemplateDAO();
			dictionaryDAO = SearchManager.getInstance().getDictionaryDAO();
			patternDAO = SearchManager.getInstance().getPatternDAO();
			
			//TODO move DateLevelLogParser to common
//			levels = DateLevelLogParser.getLevelsList();
			//TODO remove temp impl
			levels = new ArrayList<String>();
			levels.add("TRACE");
			levels.add("DEBUG");
			levels.add("INFO");
			levels.add("WARN");
			levels.add("ERROR");
			levels.add("FATAL");
		} catch (ArchiveeException e) {
			ArchiveeException.error(e, "Unable to init Search.", this);
		}
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public void searchLog() {
		try {
			searchLog(this.value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * {@inheritDoc}
	 * @throws ArchiveeException 
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.ISearch#searchLog(java.lang.String)
	 */
	@Override
	public LogQueryResponse searchLog(String searchString) throws ArchiveeException {
	
		LogQueryResponse response = new LogQueryResponse();
		
		ContextIndex contextIndex = new ContextIndex();
		contextIndex.getKey().setWord(searchString);
		
		TreeSet<ObjectId> contexts = new TreeSet<ObjectId>();
		for(ContextIndex ci : contextIndexDAO.find(contextIndex, ArchiveeConstants.CONTEXT_INDEX_WORD_QUERY)) {
			for(ObjectId id : ci.getContextSequences()) {
				contexts.add(id);
			}
		}
		
		for(ObjectId contextId : contexts) {
			Context context = new Context();
			context.setId(contextId);
			context = contextDAO.get(context);
			
			//found log data - needs to decode it
			if(context != null && context.getData().length > 0) {
				TemplateDictionary templateDictionary = new TemplateDictionary();
				templateDictionary.setContextId(contextId);
				templateDictionary = templateDictionaryDAO.get(templateDictionary);
				if(templateDictionary == null || templateDictionary.getTemplateEntries().size() <= 0) {
					ArchiveeLogger.getInstance().warn(this, "Invalid template found during search.", context, response.getLogs());
				} else {
					int index = 0;
					int offset = 0;
					int bits = 0;
					
					int levelBitsLength = ArchiveeByteUtils.getMaxBitsLength(levels.size());
					
					while(true) {
						ArrayList<Byte> bytes = new ArrayList<Byte>();
						Collections.addAll(bytes, context.getData());
						
						//Decode template
						Object object = ArchiveeByteUtils.decode(bytes,index,offset,(HashMap) templateDictionary.getTemplateEntries());
						if(object != null) {
							ObjectId templateId = (ObjectId) object;
							bits = templateDictionary.getTemplateEntries().get(templateId).getBitsLength();
							index = ArchiveeByteUtils.nextDecodeIndex(index, offset, bits);
							offset = ArchiveeByteUtils.nextDecodeOffset(index, offset, bits);
							
							Template template = new Template();
							template.setId(templateId);
							template = templateDAO.get(template);
							
							if(template != null && template.getKey().getPatternPath().getValues().size() >= 0) {
								//Decode date
								long timeRange = ArchiveeByteUtils.decode(bytes,index,offset,context.getDateBitsLenght());
								long time = context.getStartDate().getTime() + timeRange; 
								Date date = new Date(time);
								
								bits = context.getDateBitsLenght();
								index = ArchiveeByteUtils.nextDecodeIndex(index, offset, bits);
								offset = ArchiveeByteUtils.nextDecodeOffset(index, offset, bits);
								
								//Decode level
								long indexLevel = ArchiveeByteUtils.decode(bytes,index,offset,levelBitsLength);
								String level = levels.get((int)indexLevel);
								bits = levelBitsLength;
								index = ArchiveeByteUtils.nextDecodeIndex(index, offset, bits);
								offset = ArchiveeByteUtils.nextDecodeOffset(index, offset, bits);
								
								
								Pattern pattern = new Pattern();
								pattern.setId(context.getKey().getPatternId());
								pattern = patternDAO.get(pattern);
								
								if(pattern != null && pattern.getPatterns().size() > 0) {
									//Decode words
									
									PatternPath patternPath = PatternPath.getPatternPath(template.getKey().getPath());
									if(patternPath == null) {
										ArchiveeLogger.getInstance().warn(this,"PatternPath object not found during decoding.", context, bytes, index, offset, template, date, level, pattern, response.getLogs());
										break;
									}
									
									ArrayList<String> words = new ArrayList<String>();
									
									for(int i=0; i < patternPath.getValues().size(); i++) {
										PatternPathEntry entry = patternPath.getValues().get(i);
										
										for(int j=0; j < entry.getWords(); j++) {
											Dictionary dictionary = new Dictionary();
											dictionary.getKey().setElementIndex(entry.getIndex());
											dictionary.getKey().setSubElementIndex(j);
											dictionary.getKey().setTemplateId(template.getId());
											dictionary.getKey().setSequence(context.getKey().getSequence());
											
											for(Dictionary d : dictionaryDAO.find(dictionary,ArchiveeConstants.DICTIONARY_KEY_QUERY)) {
												dictionary = d;
												break;
											}
											
											if(dictionary.getId() == null) {
												ArchiveeLogger.getInstance().warn(this,"Pattern object not found during decoding.", context, bytes, index, offset, template, date, level, pattern, response.getLogs());
											}
											
											object = ArchiveeByteUtils.decode(bytes,index,offset,(HashMap) dictionary.getEntries());
											String word = (String) object;
											bits = templateDictionary.getTemplateEntries().get(templateId).getBitsLength();
											index = ArchiveeByteUtils.nextDecodeIndex(index, offset, bits);
											offset = ArchiveeByteUtils.nextDecodeOffset(index, offset, bits);
											
											words.add(word);
										}
									}

									//finally mounts the message using the words decoded within pattern path
									String message = patternPath.mountMessage(words, pattern);
									
									Log log = new Log();
									log.setDate(date);
									log.setLevel(level);
									log.setMessage(message);
									response.getLogs().add(log);
								} else {
									ArchiveeLogger.getInstance().warn(this,"Pattern object not found during decoding.", context, bytes, index, offset, template, date, level, pattern, response.getLogs());
									break;
								}
							} else {
								ArchiveeLogger.getInstance().warn(this,"Invalid template found during decoding.", context, bytes, index, offset, template, response.getLogs());
								break;
							}
						} else {
							ArchiveeLogger.getInstance().warn(this,"Invalid object found during decoding.", context, bytes, index, offset, response.getLogs());
							break;
						}
					}
				}
			} else {
				ArchiveeLogger.getInstance().warn(this,"Invalid context found during search.", context, response.getLogs());
			}			
		}
		
		logs = response.getLogs().toArray(new Log[response.getLogs().size()]);
		
		return response;
	}

	/**
	 * @return the logs
	 */
	public Log[] getLogs() {
		return logs;
	}

	/**
	 * @param logs the logs to set
	 */
	public void setLogs(Log[] logs) {
		this.logs = logs;
	}

	
}