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
package biz.bidi.archivee.components.archiver;

import java.util.ArrayList;
import java.util.Date;

import biz.bidi.archivee.commons.ArchiveeConstants;
import biz.bidi.archivee.commons.components.ArchiveeManagedComponent;
import biz.bidi.archivee.commons.components.Component;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.interfaces.IArchiver;
import biz.bidi.archivee.commons.interfaces.IArchiverSender;
import biz.bidi.archivee.commons.interfaces.ICompressorSender;
import biz.bidi.archivee.commons.model.mongodb.ContextQueue;
import biz.bidi.archivee.commons.model.mongodb.DictionaryQueue;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.commons.model.mongodb.PatternPath;
import biz.bidi.archivee.commons.model.mongodb.Template;
import biz.bidi.archivee.commons.model.xml.CompressorMessage;
import biz.bidi.archivee.commons.model.xml.PatternMessage;
import biz.bidi.archivee.commons.model.xml.enums.CompressorMessageType;
import biz.bidi.archivee.commons.utils.ArchiveeDateUtils;
import biz.bidi.archivee.commons.utils.ArchiveeLogger;
import biz.bidi.archivee.commons.utils.ArchiveePatternUtils;
import biz.bidi.archivee.components.archiver.commons.ArchiverManager;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 29, 2012
 */
public class Archiver extends ArchiveeManagedComponent implements IArchiver {

	/**
	 * The compressor sender
	 */
	protected ICompressorSender compressorSender;
	/**
	 * The archiver sender
	 */
	protected IArchiverSender archiverSender;
	
	public Archiver() {
		try {
			lockerDAO = ArchiverManager.getInstance().getLockerDAO();
			patternDAO = ArchiverManager.getInstance().getPatternDAO();
			templateDAO = ArchiverManager.getInstance().getTemplateDAO();
			dictionaryQueueDAO = ArchiverManager.getInstance().getDictionaryQueueDAO();
			contextQueueDAO = ArchiverManager.getInstance().getContextQueueDAO();
			
			compressorSender = ArchiverManager.getInstance().getCompressorSender();
			archiverSender = ArchiverManager.getInstance().getArchiverSender();
		} catch (ArchiveeException e) {
			ArchiveeException.error(e, "Error in init Compressor component.", this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see 
	 * biz.bidi.archivee.commons.interfaces.IArchiver#archiveData(biz.bidi.archivee.commons.model.xml.PatternMessage)
	 */
	@Override
	public void archiveData(PatternMessage message) throws ArchiveeException {
		if(message.getAppId() == null || message.getDate() == null || message.getLevel() == null
				|| 	message.getLevel().isEmpty() || message.getMessage() == null || message.getPatternId() == null) {
			throw new ArchiveeException("Invalid message received in archiver, discarding message.",this,message);
		}
		
		if(!acquireLock(Component.ARCHIVER.getValue(), message.getThreadId()))
		{
			archiverSender.sendPatternMessage(message);
			return;
		}
		
		try {
			Date messageDate = ArchiveeDateUtils.convertToDate(message.getDate());
			
			//Finds the pattern
			Pattern pattern = new Pattern();
			pattern.setId(message.getPatternId());
			pattern = patternDAO.get(pattern);			

			if(pattern == null || 
				pattern.getValue() == null || 
				pattern.getValue().isEmpty()) {
				throw new ArchiveeException("Invalid pattern found for message received in archiver, discarding message.",this,message,pattern);
			}
			
			PatternPath patternPath = ArchiveePatternUtils.findPatternPath(message.getMessage(), pattern);
			
			Template template = findAndSaveTemplate(patternPath, message);
			
			if(template == null) {
				throw new ArchiveeException("Invalid template found for message received in archiver, discarding message.",this,message,pattern,patternPath);
			}
			
			ContextQueue contextQueue = getContextData(message, messageDate, template);
			
			if(!saveDictionaryDataAndIsAtQueue(message, patternPath, pattern, template, contextQueue)) {
				CompressorMessage compressorMessage = new CompressorMessage();
				
				compressorMessage.setContextQueueId(contextQueue.getId());
				compressorMessage.setMessageType(CompressorMessageType.CREATE_DICTIONARY);
				compressorMessage.setThreadId(message.getThreadId());
				
				compressorSender.sendCompressorMessage(compressorMessage);
			}
			
			ArchiveeLogger.getInstance().debug(this,"Archived message in context queue.",message,template,pattern,patternPath,contextQueue);
		} catch (ArchiveeException e) {
			release(Component.ARCHIVER.getValue(), message.getThreadId());
			throw e;
		} catch (Exception e) {
			release(Component.ARCHIVER.getValue(), message.getThreadId());
			throw new ArchiveeException(e,this);
		}
		
		release(Component.ARCHIVER.getValue(), message.getThreadId());
	}


	/**
	 * @param message
	 * @param pattern
	 * @throws ArchiveeException 
	 */
	private ContextQueue getContextData(PatternMessage message, Date messageDate, Template template) throws ArchiveeException {
		ContextQueue contextQueue = new ContextQueue();
		contextQueue.getKey().setPatternId(message.getPatternId());
		contextQueue.getKey().setAtQueue(true);
		
		for(ContextQueue cq : contextQueueDAO.find(contextQueue, ArchiveeConstants.CONTEXT_QUEUE_NEXT_KEY_QUERY)) {
			contextQueue = cq;
			break;
		}
		
		if(contextQueue.getId() == null) {
			contextQueue.getKey().setSequence(new Date().getTime());
		}
		
		if(contextQueue.getStartDate() == null || contextQueue.getStartDate().compareTo(messageDate) == 1) {
			contextQueue.setStartDate(messageDate);
		}
		if(contextQueue.getEndDate() == null || contextQueue.getEndDate().compareTo(messageDate) == -1) {
			contextQueue.setEndDate(messageDate);
		}
		
		contextQueue.getMessages().add(message);
		contextQueue.setDataLength(contextQueue.getDataLength() + message.getMessage().length());
		
		int templateCounts = 0;
		if(contextQueue.getTemplateCounts().containsKey(template.getId())) {
			templateCounts = contextQueue.getTemplateCounts().get(template.getId());
		}
		templateCounts++;
		contextQueue.getTemplateCounts().put(template.getId(), templateCounts);
		
		return contextQueue;
	}

	/**
	 * @param patternPath
	 * @param message
	 * @return
	 * @throws ArchiveeException 
	 */
	private Template findAndSaveTemplate(PatternPath patternPath,PatternMessage message) throws ArchiveeException {
		Template template = new Template();
		template.getKey().setPatternId(message.getPatternId());
		template.getKey().setPatternPath(patternPath);
		
		for(Template t : templateDAO.find(template,ArchiveeConstants.TEMPLATE_KEY_QUERY)) {
			template = t;
			break;
		}
		
		if(template.getId() == null) { //if template doesn't exists
			templateDAO.save(template);
		}
		
		return template;
	}

	/**
	 * @param message 
	 * @param message
	 * @param patternPath
	 * @param words
	 * @param pattern
	 * @throws ArchiveeException 
	 */
	private boolean saveDictionaryDataAndIsAtQueue(PatternMessage message, PatternPath patternPath, Pattern pattern, Template template, ContextQueue contextQueue) throws ArchiveeException {
		
		/**
		 * Context queue and its dictionary should be sent to compressor, first to needs
		 * flag them as not at the queue to prevent concurrent access problems  
		 */
		//TODO if any dictionary size is close to 64 bits it also should remove context from queue 
		boolean isAtQueue = true;
		if(contextQueue.getDataLength() >= ArchiveeConstants.CONTEXT_QUEUE_MAX_DATA_SIZE) {
			isAtQueue = false;
		}
		
		contextQueue.getMessages().add(message);
		contextQueue.getKey().setAtQueue(isAtQueue);
		
		contextQueueDAO.save(contextQueue);
		
		ArrayList<String> words = ArchiveePatternUtils.getPatternValues(message.getMessage());		
		
		String word = "";
		int index = 0;
		int i = 0;
		while(i < words.size() && index < patternPath.getValues().size()) {
			
			int j=0;
			for(j=0; j < patternPath.getValues().get(index).getWords(); j++) {
				word = words.get(i);
				i++;
				
				DictionaryQueue dictionaryQueue = new DictionaryQueue();
				dictionaryQueue.getKey().setElementIndex(index);
				dictionaryQueue.getKey().setSubElementIndex(j);
				dictionaryQueue.getKey().setTemplateId(template.getId());
				dictionaryQueue.getKey().setSequence(contextQueue.getKey().getSequence());
				
				for(DictionaryQueue dq : dictionaryQueueDAO.find(dictionaryQueue,ArchiveeConstants.DICTIONARY_QUEUE_KEY_QUERY)) {
					dictionaryQueue = dq;
					break;
				}
				
				int count = 0;
				if(dictionaryQueue.getCounts().containsKey(word)) {
					count = dictionaryQueue.getCounts().get(word);
				}
				count++;
				dictionaryQueue.getCounts().put(word, count);
				
				dictionaryQueueDAO.save(dictionaryQueue);
			}
			index++;
		}
		
		return isAtQueue;
	}

}