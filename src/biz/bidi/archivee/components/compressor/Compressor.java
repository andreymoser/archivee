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
package biz.bidi.archivee.components.compressor;

import java.util.ArrayList;
import java.util.HashMap;

import org.bson.types.ObjectId;

import biz.bidi.archivee.commons.ArchiveeConstants;
import biz.bidi.archivee.commons.components.ArchiveeManagedComponent;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.interfaces.ICompressorSender;
import biz.bidi.archivee.commons.model.huffman.HuffmanObjectIdNode;
import biz.bidi.archivee.commons.model.huffman.HuffmanObjectIdTree;
import biz.bidi.archivee.commons.model.huffman.HuffmanWordNode;
import biz.bidi.archivee.commons.model.huffman.HuffmanWordTree;
import biz.bidi.archivee.commons.model.mongodb.Context;
import biz.bidi.archivee.commons.model.mongodb.ContextIndex;
import biz.bidi.archivee.commons.model.mongodb.ContextQueue;
import biz.bidi.archivee.commons.model.mongodb.Dictionary;
import biz.bidi.archivee.commons.model.mongodb.DictionaryEntry;
import biz.bidi.archivee.commons.model.mongodb.DictionaryQueue;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.commons.model.mongodb.PatternPath;
import biz.bidi.archivee.commons.model.mongodb.Template;
import biz.bidi.archivee.commons.model.mongodb.TemplateDictionary;
import biz.bidi.archivee.commons.model.xml.CompressorMessage;
import biz.bidi.archivee.commons.model.xml.PatternMessage;
import biz.bidi.archivee.commons.model.xml.enums.CompressorMessageType;
import biz.bidi.archivee.commons.utils.ArchiveeByteUtils;
import biz.bidi.archivee.commons.utils.ArchiveePatternUtils;
import biz.bidi.archivee.components.archiver.commons.ArchiverManager;
import biz.bidi.archivee.components.logparser.commons.LogParserManager;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Oct 2, 2012
 */
public class Compressor extends ArchiveeManagedComponent implements ICompressor {

	/**
	 * The compressor sender
	 */
	protected ICompressorSender compressorSender;
	
	public Compressor() {
		try {
			templateDAO = ArchiverManager.getInstance().getTemplateDAO();
			patternDAO = ArchiverManager.getInstance().getPatternDAO();
			
			dictionaryQueueDAO = ArchiverManager.getInstance().getDictionaryQueueDAO();
			dictionaryDAO = ArchiverManager.getInstance().getDictionaryDAO();
			
			contextQueueDAO = ArchiverManager.getInstance().getContextQueueDAO();
			contextIndexDAO = ArchiverManager.getInstance().getContextIndexDAO();
			contextDAO = ArchiverManager.getInstance().getContextDAO();

			templateDictionaryDAO = ArchiverManager.getInstance().getTemplateDictionaryDAO();
			
			compressorSender = LogParserManager.getInstance().getCompressorSender();
		} catch (ArchiveeException e) {
			ArchiveeException.error(e, "Error in init Compressor component.", this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.components.compressor.ICompressor#compressData(biz.bidi.archivee.commons.model.xml.CompressorMessage)
	 */
	@Override
	public void compressData(CompressorMessage message) throws ArchiveeException {
		if(message.getContextQueueId() == null) {
			throw new ArchiveeException("Invalid compressor message: context queue is null!",this,message);
		}

		ContextQueue contextQueue = new ContextQueue();
		contextQueue.setId(message.getContextQueueId());
		for(ContextQueue cq : contextQueueDAO.find(contextQueue, ArchiveeConstants.CONTEXT_QUEUE_KEY_QUERY)) {
			contextQueue = cq;
			break;
		}
		if(contextQueue.getKey().getPatternId() == null) { //if not found
			throw new ArchiveeException("Invalid compressor message: context queue doesn't exists!",message);
		}
		
		if(message.getMessageType().equals(CompressorMessageType.CREATE_DICTIONARY)) {
			ArrayList<Template> templates = findTemplates(contextQueue);
			
			for(Template template : templates) {
				DictionaryQueue dictionaryQueue = new DictionaryQueue();
				dictionaryQueue.getKey().setTemplateId(template.getId());
				dictionaryQueue.getKey().setAtQueue(false);
				dictionaryQueue.getKey().setSequence(contextQueue.getKey().getSequence());
				
				for(DictionaryQueue dq : dictionaryQueueDAO.find(dictionaryQueue, ArchiveeConstants.DICTIONARY_QUEUE_KEY_QUERY)) {
					createDictionary(template, dq);
					
					dictionaryQueueDAO.delete(dq,null);
				}
			}
			
			message.setMessageType(CompressorMessageType.COMPRESS_DATA);
			compressorSender.sendCompressorMessage(message);
		}
		
		if(message.getMessageType().equals(CompressorMessageType.COMPRESS_DATA)) {
			
			compressContextQueueData(message, contextQueue);
			contextQueueDAO.delete(contextQueue, null);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void compressContextQueueData(CompressorMessage message, ContextQueue contextQueue) throws ArchiveeException {
		Pattern pattern = new Pattern();
		pattern.setId(contextQueue.getKey().getPatternId());
		for(Pattern p : patternDAO.find(pattern)) {
			pattern = p;
			break;
		}
		//TODO validate pattern - it should not save on error

		Context context = new Context();
		context.getKey().setAppId(pattern.getAppId());
		context.getKey().setPatternId(contextQueue.getKey().getPatternId());
		context.getKey().setSequence(contextQueue.getKey().getSequence());
		context.setStartDate(contextQueue.getStartDate());
		
		TemplateDictionary templateDictionary = new TemplateDictionary();
		templateDictionary.setContextId(contextQueue.getId());
		createTemplateDictionary(templateDictionary, contextQueue);

		ArrayList<Byte> data = new ArrayList<Byte>();
		
		int bitOffset = 0;
		
		for(PatternMessage patternMessage : contextQueue.getMessages()) {
			PatternPath patternPath = ArchiveePatternUtils.findPatternPath(patternMessage.getMessage(), pattern);
			
			//TODO validate pattern path - it should not save on error
			//TODO validate pattern id, it should not differ
			
			Template template = findTemplate(patternPath, pattern);
			
			bitOffset = ArchiveeByteUtils.append(template.getId(), data, bitOffset, (HashMap) templateDictionary.getTemplateEntries());
			
			ArrayList<String> words = ArchiveePatternUtils.getPatternValues(patternMessage.getMessage());		
			
			for(int i = 0; i < words.size(); i++) {
				String word = words.get(i);
				
				PatternPath patternPath2 = new PatternPath();
				patternPath2.getValues().addAll(patternPath.getValues());
				for(int j=i+1; j < patternPath.getValues().size(); j++) {
					patternPath2.getValues().remove(j);
				}
				
				int j=0;
				for(j=0; j < patternPath2.getValues().get(i).getWords(); j++) {
					Dictionary dictionary = new Dictionary();
					dictionary.getKey().setElementIndex(i);
					dictionary.getKey().setSubElementIndex(j);
					dictionary.getKey().setTemplateId(template.getId());
					dictionary.getKey().setSequence(contextQueue.getKey().getSequence());
					
					for(Dictionary dq : dictionaryDAO.find(dictionary, ArchiveeConstants.DICTIONARY_KEY_QUERY)) {
						dictionary = dq;
						break;
					}
					//TODO validate if not found
					
					bitOffset = ArchiveeByteUtils.append(word, data, bitOffset, (HashMap) dictionary.getEntries());
					
					i++;
					if(i >= words.size()) {
						break;
					}
					
					//Saves context index data
					ContextIndex contextIndex = new ContextIndex();
					contextIndex.getKey().setAppId(pattern.getAppId());
					contextIndex.getKey().setWord(word);
					for(ContextIndex ci : contextIndexDAO.find(contextIndex, ArchiveeConstants.CONTEXT_INDEX_LATEST_QUERY)) {
						contextIndex = ci;
						break;
					}
					//TODO validate context index
					if(contextIndex !=  null && contextIndex.getId() != null) {
						contextIndexDAO.save(contextIndex);
					}
					
					word = words.get(i);
				}
			}
			
		}
		
		context.setData(data.toArray(new Byte[data.size()]));
		contextDAO.save(context);
		
	}

	/**
	 * @param template
	 * @param dictQueue
	 * @throws ArchiveeException 
	 */
	private void createDictionary(Template template, DictionaryQueue dictQueue) throws ArchiveeException {
		Dictionary dictionary = new Dictionary();
		dictionary.getKey().setElementIndex(dictQueue.getKey().getElementIndex());
		dictionary.getKey().setTemplateId(dictQueue.getKey().getTemplateId());
		dictionary.getKey().setSequence(dictQueue.getKey().getSequence());
		
		createDictionary(dictionary, dictQueue);
		
		//TODO save context queue with template counts
	}
	
	/**
	 * @param dictionary
	 * @param dictQueue
	 * @throws ArchiveeException 
	 */
	private void createDictionary(Dictionary dictionary,DictionaryQueue dictQueue) throws ArchiveeException {
		HuffmanWordTree huffmanWordTree = new HuffmanWordTree();
		huffmanWordTree.buildTree(dictQueue.getCounts());
		
		for(String word : dictQueue.getCounts().keySet()) {
			HuffmanWordNode node = huffmanWordTree.getNode(word);
			
			if(node == null) {
				throw new ArchiveeException("Not found huffman node for dictionary creation: " + word,this,huffmanWordTree, dictQueue, dictionary);
			}
				
			DictionaryEntry dictionaryEntry = new DictionaryEntry();
			dictionaryEntry.setBitsLength(node.getLevel());
			dictionaryEntry.setBytes(node.getCode());
			dictionaryEntry.setCount(dictQueue.getCounts().get(word));
			
			dictionary.getEntries().put(word, dictionaryEntry);
			dictionary.getEntriesIndex().put(node.getCode(), word);
		}
		
		dictionaryDAO.save(dictionary);
	}
	
	/**
	 * @param message
	 * @param contextQueue
	 * @return
	 * @throws ArchiveeException 
	 */
	private void createTemplateDictionary(TemplateDictionary templateDictionary, ContextQueue contextQueue) throws ArchiveeException {
		HuffmanObjectIdTree huffmanObjectIdTree = new HuffmanObjectIdTree();
		huffmanObjectIdTree.buildTree(contextQueue.getTemplateCounts());
		
		for(ObjectId templateId : contextQueue.getTemplateCounts().keySet()) {
			HuffmanObjectIdNode node = huffmanObjectIdTree.getNode(templateId);
			
			if(node == null) {
				throw new ArchiveeException("Not found huffman node for template dictionary creation: " + templateId,this, huffmanObjectIdTree, contextQueue, templateDictionary);
			}
				
			DictionaryEntry dictionaryEntry = new DictionaryEntry();
			dictionaryEntry.setBitsLength(node.getLevel());
			dictionaryEntry.setBytes(node.getCode());
			dictionaryEntry.setCount(contextQueue.getTemplateCounts().get(templateId));
			
			templateDictionary.getTemplateEntries().put(templateId, dictionaryEntry);
			templateDictionary.getTemplateEntriesIndex().put(node.getCode(), templateId);
		}
		
		templateDictionaryDAO.save(templateDictionary);
	}

	/**
	 * @param patternPath
	 * @param message
	 * @return
	 * @throws ArchiveeException 
	 */
	private ArrayList<Template> findTemplates(ContextQueue contextQueue) throws ArchiveeException {
		ArrayList<Template> templates = new ArrayList<Template>();
		
		Template template = new Template();
		template.getKey().setPatternId(contextQueue.getKey().getPatternId());
		
		for(Template t : templateDAO.find(template, ArchiveeConstants.TEMPLATE_KEY_QUERY)) {
			templates.add(t);
		}
		
		return templates;
	}
	
	/**
	 * @param patternPath
	 * @param message
	 * @return
	 * @throws ArchiveeException 
	 */
	private Template findTemplate(PatternPath patternPath,Pattern pattern) throws ArchiveeException {
		Template template = new Template();
		template.getKey().setPatternId(pattern.getId());
		template.getKey().setPatternPath(patternPath);
		
		for(Template t : templateDAO.find(template, ArchiveeConstants.TEMPLATE_KEY_QUERY)) {
			template = t;
			break;
		}
		
		return template;
	}

}