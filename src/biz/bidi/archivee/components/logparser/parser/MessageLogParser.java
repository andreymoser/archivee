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

import biz.bidi.archivee.commons.dao.IArchiveeGenericDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.interfaces.ILogParser;
import biz.bidi.archivee.commons.model.mongodb.IPattern;
import biz.bidi.archivee.commons.model.mongodb.LogQueue;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.commons.model.mongodb.PatternChild;
import biz.bidi.archivee.commons.model.mongodb.PatternKey;
import biz.bidi.archivee.commons.model.xml.ParserMessage;
import biz.bidi.archivee.commons.utils.ArchiveePatternUtils;
import biz.bidi.archivee.components.listeners.logsender.ILogSender;
import biz.bidi.archivee.components.logparser.commons.LogParserUtils;

import com.google.code.morphia.query.Query;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 11, 2012
 */
public class MessageLogParser implements ILogParser {

	private IArchiveeGenericDAO<Pattern, Query<Pattern>> patternDAO;
	private IArchiveeGenericDAO<LogQueue, Query<LogQueue>> logQueueDAO;
	/**
	 * The log parser sender
	 */
	private ILogSender logParserSender;

	/**
	 * 
	 */
	public MessageLogParser() {
		try {
			logParserSender = LogParserUtils.getLogSender();
			patternDAO = LogParserUtils.getPatternDAO();
			logQueueDAO = LogParserUtils.getLoqQueue();
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
		logQueue.setLine(message.getMessage());
		
		Pattern pattern = findPattern(message);
		if(pattern == null) { //pattern not found
			processForNewPatterns(message); //find new patterns and stores if successful
			
			pattern = findPattern(message);
			
			if(pattern == null) { //pattern not found
				logQueueDAO.save(logQueue); // stores into log queue
			} else {
				sendLogLineToIndex(message, pattern); // send log + pattern to ArchiveeIndex and ArchiveeContext
			}
		} else { //pattern found
			LogQueue nextLogQueue = getNextLogQueue();
			if(nextLogQueue != null) { //if queue is not empty
				logQueueDAO.delete(nextLogQueue, null);
				
				System.out.println("Sent old queue: " + nextLogQueue.getId());
//				logParserSender.sendLogLine(logLine);
			}
			
			sendLogLineToIndex(message, pattern); // send log + pattern to ArchiveeIndex and ArchiveeContext
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
		
		ArrayList<LogQueue> logQueueList = (ArrayList<LogQueue>) logQueueDAO.find(new LogQueue()).asList();
		
		if(logQueueList.size() < 10) {
			return null;
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
				
				if(logQueue.getLine().equals(logLine)) {
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
					PatternKey key = new PatternKey();
					key.setValue(simpleRegexLogLine.substring(offset, offset + l));
					key.setAppId(findAppId(message.getName()));
					
					newPattern = getNextPattern(key);
					newPattern.setOffset(offset);
					break;
				}
			} else {
				if(found > 0) {
					int size = simpleRegexLogLine.substring(offset, offset + l).length();
					int sizeTail = simpleRegexLogLine.length() - offset;
					
					if((found > (logQueueList.size()/4) && size > 6) || (found > 0 && sizeTail <= 6)) {
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
		
		return newPattern;
	}

	/**
	 * @return
	 * @throws ArchiveeException 
	 */
	private Pattern getNextPattern(PatternKey key) {
		Pattern pattern = new Pattern();
		
		int count = 0;
		
		while(count < 10) {
			try {
				if(count == 0) {
					pattern.setId((int) patternDAO.getSize(pattern) + 1);
				}
				patternDAO.save(pattern);
				pattern.setAppId(1);
				pattern.setValue(key.getValue());
			} catch(ArchiveeException e) {
				count++;
				pattern.setId(pattern.getId() + 1);
				continue;
			}
			break;
		}
		
		return pattern;
	}

	/**
	 * @param name
	 * @return
	 */
	private int findAppId(String name) {
		// TODO Auto-generated method stub
		return 1;
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
			if(ArchiveePatternUtils.matchRegex(logLine, ArchiveePatternUtils.convertSimpleRegexToRegex(pattern.getValue()))) {
				patternFound = pattern;
				break;
			}
		}
		
		return patternFound;
	}

	public void sendLogLineToIndex(ParserMessage message, Pattern pattern) {
		System.out.println("Sent: " + message.getMessage());
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