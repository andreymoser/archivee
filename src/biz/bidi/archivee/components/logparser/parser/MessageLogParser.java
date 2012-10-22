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
package biz.bidi.archivee.components.logparser.parser;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import biz.bidi.archivee.commons.ArchiveeConstants;
import biz.bidi.archivee.commons.components.ArchiveeManagedComponent;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.interfaces.ILogParser;
import biz.bidi.archivee.commons.interfaces.ILogSender;
import biz.bidi.archivee.commons.interfaces.IPatternSender;
import biz.bidi.archivee.commons.model.mongodb.App;
import biz.bidi.archivee.commons.model.mongodb.IPattern;
import biz.bidi.archivee.commons.model.mongodb.LogQueue;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.commons.model.mongodb.PatternChild;
import biz.bidi.archivee.commons.model.xml.ParserMessage;
import biz.bidi.archivee.commons.model.xml.PatternMessage;
import biz.bidi.archivee.commons.utils.ArchiveeDateUtils;
import biz.bidi.archivee.commons.utils.ArchiveePatternUtils;
import biz.bidi.archivee.components.logparser.commons.LogParserManager;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 11, 2012
 */
public class MessageLogParser extends ArchiveeManagedComponent implements ILogParser {
	
	/**
	 * The log parser sender
	 */
	private ILogSender logParserSender;
	/**
	 * The pattern sender
	 */
	private IPatternSender patternSender;
	
	/**
	 * 
	 */
	public MessageLogParser() {
		try {
			logParserSender = LogParserManager.getInstance().getLogSender();
			patternSender = LogParserManager.getInstance().getPatternSender();
			
			patternDAO = LogParserManager.getInstance().getPatternDAO();
			logQueueDAO = LogParserManager.getInstance().getLoqQueueDAO();
			appDAO = LogParserManager.getInstance().getAppDAO();
		} catch (ArchiveeException e) {
			ArchiveeException.log(e, "Unable to init LogParser sucessfully",
					this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.ILogParser#parseLogLine(java.lang.String)
	 */
	@Override
	public void parseLog(ParserMessage message) throws ArchiveeException {
		LogQueue logQueue = new LogQueue();
		logQueue.setAppId(findAndSaveAppId(message.getName()));
		logQueue.setMessage(message.getMessage());
		logQueue.setLevel(message.getLevel());
		logQueue.setDate(ArchiveeDateUtils.convertToDate(message.getDate()));
		
		Pattern pattern = findPattern(message);
		if(pattern == null) { //pattern not found
			processForNewPatterns(message); //find new patterns and stores if successful
			
			pattern = findPattern(message);
			
			if(pattern == null) { //pattern not found
				logQueueDAO.save(logQueue); // stores into log queue
			} else {
				sendPatternMessage(message, pattern); // send log + pattern to ArchiveeIndex and ArchiveeContext
			}
		} else { //pattern found
			LogQueue nextLogQueue = getNextLogQueue();
			if(nextLogQueue != null) { //if queue is not empty
				logQueueDAO.delete(nextLogQueue, null);
				
				ParserMessage oldMessage = new ParserMessage();
				oldMessage.setDate(ArchiveeDateUtils.convertDateToString(logQueue.getDate()));
				oldMessage.setLevel(logQueue.getLevel());
				oldMessage.setMessage(logQueue.getMessage());
				
				logParserSender.sendLogMessage(oldMessage);
			}
			
			sendPatternMessage(message, pattern); // send log + pattern to ArchiveeIndex and ArchiveeContext
		}
	}
	
	/**
	 * @return
	 * @throws ArchiveeException 
	 */
	private LogQueue getNextLogQueue() throws ArchiveeException {
		//TODO use custom search to optmize it
		return logQueueDAO.find(new LogQueue()).get();
	}

	/**
	 * @throws ArchiveeException 
	 * 
	 */
	private void processForNewPatterns(ParserMessage message) throws ArchiveeException {
		
		Pattern rootPattern = findRootPattern(message.getMessage());
		
		if(rootPattern == null) { // root pattern not found 
			rootPattern = findCommonRootPattern(message);
			
			if(rootPattern == null) {
				return;
			}
			
			patternDAO.save(rootPattern);
		} else { // root pattern found
			
			//if found new patterns
			if(processForNewPatterns(message,rootPattern)) {
				patternDAO.save(rootPattern);
			}
		}
		
	}

	/**
	 * @param logLine
	 * @param offset
	 * @param rootPattern
	 * @return
	 * @throws ArchiveeException 
	 */
	private boolean processForNewPatterns(ParserMessage message, Pattern pattern) throws ArchiveeException {
		return processForNewPatterns(ArchiveePatternUtils.convertToSimpleRegex(message.getMessage()), message, pattern, pattern.getValue());
	}
		
	/**
	 * @param convertToSimpleRegex
	 * @param rootPattern
	 * @throws ArchiveeException 
	 */
	private boolean processForNewPatterns(String simpleRegexLogLine,ParserMessage message, Pattern pattern, String fullPattern) throws ArchiveeException {
		if(pattern == null) {
			return false;
		}
		
		if(!simpleRegexLogLine.startsWith(pattern.getValue())) {
			return false;
		}
			
		fullPattern = pattern.getValue();
		
		Pattern newPattern = null;
		PatternChild leafPattern = ArchiveePatternUtils.getLeafPattern(pattern, simpleRegexLogLine); 
		
		boolean updateRootPattern = false;
		int offset = 0;
		
		if(leafPattern != null) {
			offset = leafPattern.getOffset() + leafPattern.getValue().length();
			
			newPattern = findCommonRootPattern(simpleRegexLogLine, message, offset, leafPattern, false);
			if(newPattern != null && newPattern.getValue().length() > 0) {
				System.out.println("Added : " + newPattern.getValue() + " - leafPattern: " + leafPattern.getValue());
				
				PatternChild newPatternChild = convertToPatternChild(newPattern); 
				leafPattern.getPatterns().add(newPatternChild);
				
				leafPattern = newPatternChild;
				updateRootPattern = true;
			}
		} else {
			offset = pattern.getValue().length();
			
			newPattern = findCommonRootPattern(simpleRegexLogLine, message, offset, pattern, false);
			if(newPattern != null && newPattern.getValue().length() > 0) {
				System.out.println("Added : " + newPattern.getValue());
				
				PatternChild newPatternChild = convertToPatternChild(newPattern); 
				pattern.getPatterns().add(newPatternChild);
				
				leafPattern = newPatternChild;
				updateRootPattern = true;
			}
		}
		
		return updateRootPattern;
	}

	

	/**
	 * @param newPattern
	 * @return
	 */
	private PatternChild convertToPatternChild(Pattern newPattern) {
		PatternChild patternChild = new PatternChild();
		
		patternChild.setOffset(newPattern.getOffset());
		patternChild.setValue(newPattern.getValue());
		patternChild.setPatterns(newPattern.getPatterns());
		
		return patternChild;
	}

	/**
	 * @param logLine
	 * @return
	 * @throws ArchiveeException 
	 */
	private Pattern findCommonRootPattern(ParserMessage message) throws ArchiveeException {
		String simpleRegexLogLine = ArchiveePatternUtils.convertToSimpleRegex(message.getMessage());
		return findCommonRootPattern(simpleRegexLogLine, message, 0, null, true);
	}
	
	/**
	 * @param simpleRegexLogLineTemp
	 * @return
	 * @throws ArchiveeException 
	 */
	private Pattern findCommonRootPattern(String simpleRegexLogLine,ParserMessage message, int offset, IPattern pattern, boolean isRoot) throws ArchiveeException {
		String logLine = message.getMessage();
		
		Pattern newPattern = null;
		
		LogQueue logQueueQuery = new LogQueue();
		logQueueQuery.setAppId(findAndSaveAppId(message.getName()));
		ArrayList<LogQueue> logQueueList = null;
		String query = null;
		if(isRoot) {
			query = ArchiveeConstants.LOG_QUEUE_APP_QUERY;
		} else {
			query = ArchiveeConstants.LOG_QUEUE_PATTERN_QUERY;
			logQueueQuery.setSimpleRegex(simpleRegexLogLine.substring(0,offset + 1));
		}
		logQueueList = (ArrayList<LogQueue>) logQueueDAO.find(logQueueQuery,query).asList();
		
		if(isRoot) {
			if(logQueueList.size() < 10) {
				return null;
			}
		}		
		
		int l = 0;
		int lMax = simpleRegexLogLine.length();
		
		while(true) {
			
			int found = 0;
			boolean hasInAll = true;
			
			for(LogQueue logQueue : logQueueList) {
				if(lMax < logQueue.getSimpleRegex().length()) {
					lMax = logQueue.getSimpleRegex().length();
				}
				
				if(logQueue.getMessage().equals(logLine)) {
					continue;
				}
				if((offset + l) >= simpleRegexLogLine.length() || (offset + l) >= logQueue.getSimpleRegex().length()) {
					continue;
				}
				if(logQueue.getSimpleRegex().startsWith(simpleRegexLogLine.substring(offset, offset + l + 1),offset)) {
					continue;
				} else {
					if(!isRoot) {
						PatternChild existentPattern = pattern.findPattern(simpleRegexLogLine.substring(offset, offset + l));						
						if(existentPattern != null) { //if exists pattern
							found--;
							break;
						}
					}
					
					found++;
					
					if(isRoot) {
						hasInAll = false;
						break;
					}
				}
			}
			
			if(isRoot) {
				if(!hasInAll) {
					newPattern = new Pattern();
					newPattern.setOffset(offset);
					newPattern.setValue(simpleRegexLogLine.substring(offset, offset + l));
					newPattern.setAppId(findAndSaveAppId(message.getName()));
					break;
				}
			} else {
				if(l > 0) {
					int offsetFinal = ((offset + l) < simpleRegexLogLine.length())?offset + l:simpleRegexLogLine.length();
					int size = simpleRegexLogLine.substring(offset, offsetFinal).length();
					int sizeTail = simpleRegexLogLine.length() - offset;
					
					if((found > (logQueueList.size()/4) && size > 6) || 
							(found > 0 && sizeTail <= 6) || 
							(found < logQueueList.size() && offsetFinal == simpleRegexLogLine.length())) {
						
						newPattern = new Pattern();
						newPattern.setValue(simpleRegexLogLine.substring(offset, offset + l));
						newPattern.setOffset(offset);
						
						break;
					}
					
					found = 0;
				}
			}
			
			l++;
			
			if(l >= lMax) {
				break;
			}
		}
		
		if(!isRoot && newPattern != null && logQueueList.size() >= 1) {
			
			int newOffset = newPattern.getOffset() + newPattern.getValue().length(); 
			
			if(newOffset < simpleRegexLogLine.length()) {
				Pattern newPatternChild = findCommonRootPattern(simpleRegexLogLine, message, newOffset, newPattern, false);
				
				if(newPatternChild != null) {
					System.out.println("\tAdded : " + newPatternChild.getValue() + " - parent: " + newPattern.getValue());
					
					PatternChild patternChild = convertToPatternChild(newPatternChild); 
					newPattern.getPatterns().add(patternChild);
				}
			}
		}
		
		return newPattern;
	}
	
	/**
	 * @param name
	 * @return
	 * @throws ArchiveeException 
	 */
	private ObjectId findAndSaveAppId(String name) throws ArchiveeException {
		ObjectId appId = null;
		
		App app = new App();
		app.setName(name);
		
		for(App app2 : appDAO.find(app)) {
			appId = app2.getId();
			break;
		}
		
		if(appId == null) {
			appId = (ObjectId) appDAO.save(app).getId();
		}
		
		return appId;
	}

	/**
	 * @param message
	 * @return
	 * @throws ArchiveeException 
	 */
	private Pattern findPattern(ParserMessage message) throws ArchiveeException {
		Pattern patternFound = findRootPattern(message.getMessage());
		
		if(patternFound == null) {
			return patternFound;
		}
		
		if(!ArchiveePatternUtils.validatePatternForLogLine(message.getMessage(),patternFound)) {
			patternFound = null;
		}
		
		return patternFound;
	}

	/**
	 * @param logLine
	 * @return
	 * @throws ArchiveeException 
	 */
	private Pattern findRootPattern(String logLine) throws ArchiveeException {
		Pattern patternFound = null;
		
		for(Pattern pattern : patternDAO.find(new Pattern())) {
			if(pattern != null && ArchiveePatternUtils.matchRegex(logLine, ArchiveePatternUtils.convertSimpleRegexToRegex(pattern.getValue()))) {
				patternFound = pattern;
				break;
			}
		}
		
		return patternFound;
	}

	public void sendPatternMessage(ParserMessage message, Pattern pattern) {
		PatternMessage patternMessage = new PatternMessage();
		try {
			patternMessage.setAppId(pattern.getAppId());
			patternMessage.setPatternId(pattern.getId());
			patternMessage.setDate(message.getDate());
			patternMessage.setLevel(message.getLevel());
			patternMessage.setMessage(message.getMessage());
			
			patternSender.sendPatternMessage(patternMessage);
		} catch (ArchiveeException e) {
			ArchiveeException.log(e, "Error while sending pattern message", patternMessage, message, pattern);
		}
	}
	
	/**
	 * @throws ArchiveeException 
	 * 
	 */
	public void showPatterns() throws ArchiveeException {
		for(Pattern p : patternDAO.find(new Pattern())) {
			System.out.println(p.getTreeStringData());
		}		
	}
}