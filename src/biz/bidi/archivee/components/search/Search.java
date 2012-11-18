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
import biz.bidi.archivee.commons.properties.ArchiveeProperties;
import biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader;
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
public class Search extends ArchiveeManagedComponent implements ISearch, IArchiveePropertiesLoader {
	
	private String value;
	
	private Log[] logs = null;
	
	private String status = "";
	
	/**
	 * Property value - used for custom replacement on any message
	 */
	private String stringReplace = "";
	/**
	 * Property value - used for custom replacement on any message
	 */
	private String stringReplaceValue = "";
	
	/**
	 * The level values
	 */
	protected ArrayList<String> levels; 

	public Search() {
		try {
			loadProperties(null);
			
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
			levels.add("INFO");
			levels.add("WARN");
			levels.add("ERROR");
			levels.add("DEBUG");
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
	
	public void searchContext() {
		try {
			searchContext(this.value);
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
		
		long currentTime = System.currentTimeMillis(); 
		
		LogQueryResponse response = new LogQueryResponse();
		
		ContextIndex contextIndex = new ContextIndex();
		contextIndex.getKey().setWord(searchString);
		
		TreeSet<ObjectId> contexts = new TreeSet<ObjectId>();
		for(ContextIndex ci : contextIndexDAO.find(contextIndex, ArchiveeConstants.CONTEXT_INDEX_CONTAINS_WORD_QUERY)) {
			for(ObjectId id : ci.getContextSequences()) {
				contexts.add(id);
			}
		}
		
		//TODO paging mechanism
		int paging = 10;
		int logsRead = 0;
		
		for(ObjectId contextId : contexts) {
			Context context = new Context();
			context.setId(contextId);
			context = contextDAO.get(context);
			
			ArrayList<Log> logsDecoded = decodeLogs(context,false);
			
			for(Log log : logsDecoded) {
				logsRead++;
				
				if(log.getMessage().toLowerCase().contains(searchString.toLowerCase())) {
					
					if(!stringReplace.isEmpty() && !stringReplaceValue.isEmpty()) {
						log.setMessage(log.getMessage().replaceAll(stringReplace, stringReplaceValue));
					}
					
					response.getLogs().add(log);
				}
				
				if(response.getLogs().size() >= paging) {
					break;
				}
			}
			
			if(response.getLogs().size() >= paging) {
				break;
			}
		}
		
		logs = response.getLogs().toArray(new Log[response.getLogs().size()]);
		
		float seconds = (System.currentTimeMillis() - currentTime)/1000;
		
		if(logs != null & logs.length > 0) {
			status = "Found " + logs.length + " entries in " + seconds + " seconds (" + logsRead + " read).";
		} else {
			status = "No entries founds (" + seconds + " seconds).";
		}
		
		return response;
	}

	public LogQueryResponse searchContext(String contextId) throws ArchiveeException {
		
		long currentTime = System.currentTimeMillis(); 
		
		LogQueryResponse response = new LogQueryResponse();
		
		ObjectId objectId = new ObjectId(contextId);
		
		//TODO paging mechanism
		int paging = 10;
		int logsRead = 0;
		
		Context context = new Context();
		context.setId(objectId);
		context = contextDAO.get(context);
		
		ArrayList<Log> logsDecoded = decodeLogs(context,true);
		
		for(Log log : logsDecoded) {
			logsRead++;
			
			response.getLogs().add(log);			
		}
		
		logs = response.getLogs().toArray(new Log[response.getLogs().size()]);
		
		float seconds = (System.currentTimeMillis() - currentTime)/1000;
		
		if(logs != null & logs.length > 0) {
			status = "Found " + logs.length + " entries in " + seconds + " seconds (" + logsRead + " read).";
		} else {
			status = "No entries founds (" + seconds + " seconds).";
		}
		
		return response;
	}
	
	/**
	 * @param context
	 * @return
	 * @throws ArchiveeException 
	 */
	private ArrayList<Log> decodeLogs(Context context, boolean isDebug) throws ArchiveeException {
		//found log data - needs to decode it
		if(context == null || context.getData().length <= 0) {
			ArchiveeLogger.getInstance().warn(this,"Invalid context found during decoding.", context);
			return null; 
		}
		
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		Collections.addAll(bytes, context.getData());
		
		ArrayList<Log> logsDecoded = new ArrayList<Log>();

		String debug = "";
		if(isDebug) {
			debug = "Context: " + context.getId().toString() + " [" + ArchiveeByteUtils.convertToBitsString(context.getData()) + "] ";
			Log log = new Log();
			log.setMessage(debug);
			logsDecoded.add(log);
		}
		
		TemplateDictionary templateDictionary = new TemplateDictionary();
		templateDictionary.setContextId(context.getId());
		templateDictionary = templateDictionaryDAO.get(templateDictionary);		
		
		if(templateDictionary == null || templateDictionary.getTemplateEntries().size() <= 0) {
			ArchiveeLogger.getInstance().warn(this, "Invalid template found during search.", context);
			return null; 
		} 
		
		int index = 0;
		int offset = 0;
		int index_aux = 0;
		int offset_aux = 0;
		
		int bits = 0;
		long code = 0;
		Date date = null;
		String level = "";
		String message = "";
		
		int levelBitsLength = ArchiveeByteUtils.getMaxBitsLength(levels.size());
		
		while(index < bytes.size()) {
			if(index == (bytes.size() -1) && offset >= context.getOffsetEnd()) {
				break;
			}
			
			try {
			
				//Decode template
				Object object = ArchiveeByteUtils.decode(bytes,index,offset,(HashMap) templateDictionary.getTemplateEntries());
				if(object != null) {
					ObjectId templateId = (ObjectId) object;
					bits = templateDictionary.getTemplateEntries().get(templateId).getBitsLength();
					code = templateDictionary.getTemplateEntries().get(templateId).getBytes();
					if(isDebug) {
						debug = "Template: " + templateId.toString() + "[" + ArchiveeByteUtils.convertToBitsString(code, bits) + ":{" + index+":" + offset +"}] "; 
						Log log = new Log();
						log.setMessage(debug);
						logsDecoded.add(log);
					}
					
					index_aux = ArchiveeByteUtils.nextDecodeIndex(index, offset, bits);
					offset_aux = ArchiveeByteUtils.nextDecodeOffset(index, offset, bits);
					index = index_aux;
					offset = offset_aux;
					
					Template template = new Template();
					template.setId(templateId);
					template = templateDAO.get(template);
					
					if(template != null && template.getKey().getPatternPath().getValues().size() >= 0) {
						//Decode date
						long timeRange = ArchiveeByteUtils.decode(bytes,index,offset,context.getDateBitsLenght());
						long time = context.getStartDate().getTime() + timeRange; 
						date = new Date(time);
						
						bits = context.getDateBitsLenght();
						code = timeRange;
						if(isDebug) {
							debug = "Date: " + context.getStartDate() + " + " + timeRange + " [" + ArchiveeByteUtils.convertToBitsString(code, bits) + ":{" + index+":" + offset +"}] "; 
							Log log = new Log();
							log.setMessage(debug);
							logsDecoded.add(log);
						}
						
						index_aux = ArchiveeByteUtils.nextDecodeIndex(index, offset, bits);
						offset_aux = ArchiveeByteUtils.nextDecodeOffset(index, offset, bits);
						index = index_aux;
						offset = offset_aux;
						
						//Decode level
						long indexLevel = ArchiveeByteUtils.decode(bytes,index,offset,levelBitsLength);
						level = levels.get((int)indexLevel);
						bits = levelBitsLength;
						code = indexLevel;
						if(isDebug) {
							debug = "Level: " + levels + " (index) => " + indexLevel + " [" + ArchiveeByteUtils.convertToBitsString(code, bits) + ":{" + index+":" + offset +"}] "; 
							Log log = new Log();
							log.setMessage(debug);
							logsDecoded.add(log);
						}
						
						index_aux = ArchiveeByteUtils.nextDecodeIndex(index, offset, bits);
						offset_aux = ArchiveeByteUtils.nextDecodeOffset(index, offset, bits);
						index = index_aux;
						offset = offset_aux;
						
						Pattern pattern = new Pattern();
						pattern.setId(context.getKey().getPatternId());
						pattern = patternDAO.get(pattern);
						
						if(pattern != null && pattern.getPatterns().size() > 0) {
							//Decode words
							
							PatternPath patternPath = PatternPath.getPatternPath(template.getKey().getPath());
							if(patternPath == null) {
								ArchiveeLogger.getInstance().warn(this,"PatternPath object not found during decoding.", context, bytes, index, offset, template, date, level, pattern);
								break;
							}
							
							ArrayList<String> words = new ArrayList<String>();

							if(isDebug) {
								debug = "";
							}
							
							for(int i=0; i < patternPath.getValues().size(); i++) {
								PatternPathEntry entry = patternPath.getValues().get(i);
								
								for(int j=0; j < entry.getWords(); j++) {
									Dictionary dictionary = new Dictionary();
									dictionary.getKey().setElementIndex(i);
									dictionary.getKey().setSubElementIndex(j);
									dictionary.getKey().setTemplateId(template.getId());
									dictionary.getKey().setSequence(context.getKey().getSequence());
									
									for(Dictionary d : dictionaryDAO.find(dictionary,ArchiveeConstants.DICTIONARY_KEY_QUERY)) {
										dictionary = d;
										break;
									}
									
									if(dictionary.getId() == null) {
										ArchiveeLogger.getInstance().warn(this,"Pattern object not found during decoding.", context, bytes, index, offset, template, date, level, pattern);
									}
									
									object = ArchiveeByteUtils.decode(bytes,index,offset,(HashMap) dictionary.getEntries());
									String word = (String) object;
									bits = dictionary.getEntries().get(word).getBitsLength();
									code = dictionary.getEntries().get(word).getBytes();
									if(isDebug) {
										debug = debug + "Dict: " + dictionary.getId() + " => " + object + " [" + ArchiveeByteUtils.convertToBitsString(code, bits) + ":{" + index+":" + offset +"}] ";										
									}
									
									index_aux = ArchiveeByteUtils.nextDecodeIndex(index, offset, bits);
									offset_aux = ArchiveeByteUtils.nextDecodeOffset(index, offset, bits);
									index = index_aux;
									offset = offset_aux;
									
									words.add(word);
								}
							}
							
							if(isDebug) {
								Log log = new Log();
								log.setMessage(debug);
								logsDecoded.add(log);
							}

							//finally mounts the message using the words decoded within pattern path
							message = patternPath.mountMessage(words, pattern);
							
							Log log = new Log();
							log.setDate(date);
							log.setLevel(level);
							log.setMessage(message);
							
							logsDecoded.add(log);
						} else {
							ArchiveeLogger.getInstance().warn(this,"Pattern object not found during decoding.", context, bytes, index, offset, template, date, level, pattern);
							break;
						}
					} else {
						ArchiveeLogger.getInstance().warn(this,"Invalid template found during decoding.", context, bytes, index, offset, template);
						break;
					}
				} else {
					ArchiveeLogger.getInstance().warn(this,"Invalid object found during decoding.", context, bytes, index, offset);
					break;
				}
			} catch (Exception e) {
				if(isDebug) {
					Log log = new Log();
					log.setMessage(debug);
					logsDecoded.add(log);
				}
				
				ArchiveeLogger.getInstance().info("Unable to decode message.","Error: " + e.getMessage() + message);
				
				Log log = new Log();
				log.setDate(date);
				log.setLevel(level);
				log.setMessage("Unable to decode message sucessfully. Index: " + index + " Offset:" + offset + " - Error: " + e.getMessage() + message);
				
				logsDecoded.add(log);
			}	
		}
		
		return logsDecoded;
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
	
	/**
	 * Defines if should display results or not
	 * @return
	 */
	public boolean isRendered() {
		return (this.logs != null && this.logs.length > 0);
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the stringReplace
	 */
	public String getStringReplace() {
		return stringReplace;
	}

	/**
	 * @param stringReplace the stringReplace to set
	 */
	public void setStringReplace(String stringReplace) {
		this.stringReplace = stringReplace;
	}

	/**
	 * @return the stringReplaceValue
	 */
	public String getStringReplaceValue() {
		return stringReplaceValue;
	}

	/**
	 * @param stringReplaceValue the stringReplaceValue to set
	 */
	public void setStringReplaceValue(String stringReplaceValue) {
		this.stringReplaceValue = stringReplaceValue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader#loadProperties(java.lang.String)
	 */
	@Override
	public void loadProperties(String prefixKey) throws ArchiveeException {
		ArchiveeProperties.loadProperties(this);
	}
	
}