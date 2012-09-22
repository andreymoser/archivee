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
package biz.bidi.archivee.components.logparser;

import java.util.ArrayList;

import biz.bidi.archivee.commons.dao.IArchiveeGenericDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.LogQueue;
import biz.bidi.archivee.commons.model.Pattern;
import biz.bidi.archivee.commons.model.PatternType;
import biz.bidi.archivee.commons.utils.ArchiveePatternUtils;
import biz.bidi.archivee.components.listeners.logsender.ILogSender;
import biz.bidi.archivee.components.logparser.commons.LogParserUtils;

import com.google.code.morphia.query.Query;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 11, 2012
 */
public class LogParser implements ILogParser {

	private IArchiveeGenericDAO<Pattern, Query<Pattern>> patternDAO;
	private IArchiveeGenericDAO<LogQueue, Query<LogQueue>> logQueueDAO;
	/**
	 * The log parser sender
	 */
	private ILogSender logParserSender;

	/**
	 * 
	 */
	public LogParser() {
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
	 * @see biz.bidi.archivee.components.logparser.ILogParser#parseLogLine(java.lang.String)
	 */
	@Override
	public void parseLogLine(String logLine) throws ArchiveeException {
		LogQueue logQueue = new LogQueue();
		logQueue.setLine(logLine);
		
		Pattern pattern = findPattern(logLine);
		if(pattern == null) { //pattern not found
			processForNewPatterns(logLine); //find new patterns and stores if successful
			
			pattern = findPattern(logLine);
			
			if(pattern == null) { //pattern not found
				logQueueDAO.save(logQueue); // stores into log queue
			} else {
				sendLogLineToIndex(logLine, pattern); // send log + pattern to ArchiveeIndex and ArchiveeContext
			}
		} else { //pattern found
			LogQueue nextLogQueue = getNextLogQueue();
			if(nextLogQueue != null) { //if queue is not empty
				logQueueDAO.delete(nextLogQueue, null);
				
				System.out.println("Sent old queue: " + nextLogQueue.getId());
//				logParserSender.sendLogLine(logLine);
			}
			
			sendLogLineToIndex(logLine, pattern); // send log + pattern to ArchiveeIndex and ArchiveeContext
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
	private void processForNewPatterns(String logLine) throws ArchiveeException {
		
		Pattern rootPattern = findRootPattern(logLine);
		
		if(rootPattern == null) { // root pattern not found 
			rootPattern = findCommonRootPattern(logLine);
			
			if(rootPattern == null) {
				return;
			}
			
			patternDAO.save(rootPattern);
		} else { // root pattern found
			
			//if found new patterns
			if(processForNewPatterns(logLine,0,rootPattern)) {
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
	private boolean processForNewPatterns(String logLine, int offset,Pattern pattern) throws ArchiveeException {
		return processForNewPatterns(ArchiveePatternUtils.convertToSimpleRegex(logLine), logLine, offset, pattern, pattern.getValue());
	}
		
	/**
	 * @param convertToSimpleRegex
	 * @param rootPattern
	 * @throws ArchiveeException 
	 */
	private boolean processForNewPatterns(String simpleRegexLogLine,String logLine,int offset, Pattern pattern, String fullPattern) throws ArchiveeException {
		if(pattern == null || pattern.isDate() || pattern.isLevel()) {
			return false;
		}
		
		if(pattern.isRoot()) {
			if(offset > 0) {
				return false;
			}
			
			if(!simpleRegexLogLine.startsWith(pattern.getValue())) {
				return false;
			}
			
			fullPattern = pattern.getValue();
		}
		
		Pattern newPattern = null;
		Pattern leafPattern = ArchiveePatternUtils.getLeafPattern(pattern, simpleRegexLogLine); 
		
		boolean updateRootPattern = false;
		
		//TODO verify final pattern matching
		int times = 0;
		while(true) {
			if(leafPattern != null) {
				offset = leafPattern.getOffset() + leafPattern.getValue().length();
				
				newPattern = findCommonRootPattern(simpleRegexLogLine, logLine, offset, leafPattern, false);
				if(newPattern != null && newPattern.getValue().length() > 0) {
					System.out.println("Added : " + newPattern.getValue());
					leafPattern.getPatterns().add(newPattern);
					
					leafPattern = newPattern;
					updateRootPattern = true;
				}
			} else {
				offset = pattern.getValue().length();
				
				newPattern = findCommonRootPattern(simpleRegexLogLine, logLine, offset, pattern, false);
				if(newPattern != null && newPattern.getValue().length() > 0) {
					System.out.println("Added : " + newPattern.getValue());
					pattern.getPatterns().add(newPattern);
					
					leafPattern = newPattern;
					updateRootPattern = true;
				}
			}
			
			if(newPattern == null || times > 50) {
				break;
			}
			times++;
		}
		
		return updateRootPattern;
	}

	

	/**
	 * @param logLine
	 * @return
	 * @throws ArchiveeException 
	 */
	private Pattern findCommonRootPattern(String logLine) throws ArchiveeException {
		String simpleRegexLogLine = ArchiveePatternUtils.convertToSimpleRegex(logLine);
		return findCommonRootPattern(simpleRegexLogLine, logLine, 0, null, true);
	}
	
	/**
	 * @param simpleRegexLogLineTemp
	 * @return
	 * @throws ArchiveeException 
	 */
	private Pattern findCommonRootPattern(String simpleRegexLogLine,String logLine, int offset, Pattern pattern, boolean isRoot) throws ArchiveeException {
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
						Pattern existentPattern = pattern.findPattern(simpleRegexLogLine.substring(offset, offset + l));						
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
					newPattern.setPatternType(PatternType.root());
					newPattern.setValue(simpleRegexLogLine.substring(offset, offset + l));
					newPattern.setOffset(offset);
					break;
				}
			} else {
				if(found > 0) {
					int size = simpleRegexLogLine.substring(offset, offset + l).length();
					int sizeTail = simpleRegexLogLine.length() - offset;
					
					if((found > (logQueueList.size()/4) && size > 6) || (found > 0 && sizeTail <= 6)) {
						newPattern = new Pattern();
						newPattern.setPatternType(PatternType.pattern());
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
	 * @param logLine
	 * @return
	 * @throws ArchiveeException 
	 */
	private Pattern findPattern(String logLine) throws ArchiveeException {
		Pattern patternFound = findRootPattern(logLine);
		
		if(patternFound == null) {
			return patternFound;
		}
		
		if(!ArchiveePatternUtils.validatePatternForLogLine(logLine,patternFound)) {
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
		
		for(Pattern pattern : patternDAO.find(new Pattern(),"all.root")) {
			if(ArchiveePatternUtils.matchRegex(logLine, ArchiveePatternUtils.convertSimpleRegexToRegex(pattern.getValue()))) {
				patternFound = pattern;
				break;
			}
		}
		
		return patternFound;
	}

	public void sendLogLineToIndex(String logLine, Pattern pattern) {
		System.out.println("Sent: " + logLine);
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