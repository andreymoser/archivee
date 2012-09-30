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

import biz.bidi.archivee.commons.components.ArchiveeManagedComponent;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.mongodb.ContextQueue;
import biz.bidi.archivee.commons.model.mongodb.DictionaryQueue;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.commons.model.mongodb.PatternPath;
import biz.bidi.archivee.commons.model.mongodb.Template;
import biz.bidi.archivee.commons.model.xml.PatternMessage;
import biz.bidi.archivee.commons.utils.ArchiveeDateUtils;
import biz.bidi.archivee.commons.utils.ArchiveePatternUtils;
import biz.bidi.archivee.components.archiver.commons.ArchiverManager;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 29, 2012
 */
public class Archiver extends ArchiveeManagedComponent implements IArchiver {

	public Archiver() {
		try {
			patternDAO = ArchiverManager.getInstance().getPatternDAO();
			templateDAO = ArchiverManager.getInstance().getTemplate();
			dictionaryQueueDAO = ArchiverManager.getInstance().getDictionaryQueue();
			contextQueueDAO = ArchiverManager.getInstance().getContextQueue();
		} catch (ArchiveeException e) {
			ArchiveeException.log(e, "Error in init Archiver component.", this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.components.archiver.IArchiver#archiveData(biz.bidi.archivee.commons.model.xml.PatternMessage)
	 */
	@Override
	public void archiveData(PatternMessage message) throws ArchiveeException {
		if(message.getAppId() == null || message.getDate() == null || message.getLevel() == null
				|| 	message.getLevel().isEmpty() || message.getMessage() == null || message.getPatternId() == null) {
			throw new ArchiveeException("Invalid message received in archiver, discarding message.",this,message);
		}
		
		Date messageDate = ArchiveeDateUtils.convertToDate(message.getDate());
		
		//Finds the pattern
		Pattern pattern = new Pattern();
		pattern.setId(message.getPatternId());
		for(Pattern p : patternDAO.find(pattern)) {
			pattern = p;
			break;
		}
		if(pattern.getValue() == null || pattern.getValue().isEmpty()) {
			throw new ArchiveeException("Invalid pattern found for message received in archiver, discarding message.",this,message,pattern);
		}
		
		PatternPath patternPath = ArchiveePatternUtils.findPatternPath(message.getMessage(), pattern);
		
		Template template = findAndSaveTemplate(patternPath, message);
		
		if(template == null) {
			throw new ArchiveeException("Invalid template found for message received in archiver, discarding message.",this,message,pattern,patternPath);
		}
		
		ArrayList<String> words = ArchiveePatternUtils.getPatternValues(message.getMessage());
		
		saveDictionaryData(words, patternPath, pattern, template);
		
		saveContextData(message, messageDate);
		
		//TODO verify queues and trigger context archiver component
		checkQueues();
	}

	/**
	 * 
	 */
	private void checkQueues() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param message
	 * @param pattern
	 * @throws ArchiveeException 
	 */
	private void saveContextData(PatternMessage message, Date messageDate) throws ArchiveeException {
		ContextQueue contextQueue = new ContextQueue();
		contextQueue.setPatternId(message.getPatternId());
		
		for(ContextQueue cq : contextQueueDAO.find(contextQueue)) {
			contextQueue = cq;
			break;
		}
		
		if(contextQueue.getStartDate() == null || contextQueue.getStartDate().compareTo(messageDate) == -1) {
			contextQueue.setStartDate(messageDate);
		}
		if(contextQueue.getEndDate() == null || contextQueue.getEndDate().compareTo(messageDate) == 1) {
			contextQueue.setEndDate(messageDate);
		}
		
		contextQueue.getMessages().add(message);
		
		contextQueueDAO.save(contextQueue);
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
		
		for(Template t : templateDAO.find(template)) {
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
	 * @param patternPath
	 * @param words
	 * @param pattern
	 * @throws ArchiveeException 
	 */
	private void saveDictionaryData(ArrayList<String> words, PatternPath patternPath, Pattern pattern, Template template) throws ArchiveeException {
		for(int i = 0; i < words.size(); i++) {
			String word = words.get(i);
			
			PatternPath patternPath2 = new PatternPath();
			patternPath2.getValues().addAll(patternPath.getValues());
			for(int j=i+1; j < patternPath.getValues().size(); j++) {
				patternPath2.getValues().remove(j);
			}
			
			int j=0;
			for(j=0; j < patternPath2.getValues().get(i).getWords(); j++) {
				DictionaryQueue dictionaryQueue = new DictionaryQueue();
				dictionaryQueue.getKey().setElementIndex(j);
				dictionaryQueue.getKey().setTemplateId(template.getId());
				
				for(DictionaryQueue dq : dictionaryQueueDAO.find(dictionaryQueue)) {
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
				
				word = words.get(i + j);
			}
			if(j <= 0) {
				i++;
			} else {
				i = i + j;
			}
		}
	}
	
}