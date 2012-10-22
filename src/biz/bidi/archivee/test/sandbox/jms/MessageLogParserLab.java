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
package biz.bidi.archivee.test.sandbox.jms;

import java.io.File;

import biz.bidi.archivee.commons.dao.mongodb.dao.PatternDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.commons.model.xml.ParserMessage;
import biz.bidi.archivee.commons.properties.ArchiveeProperties;
import biz.bidi.archivee.commons.utils.ArchiveePatternUtils;
import biz.bidi.archivee.components.listeners.parser.DateLevelLogParser;
import biz.bidi.archivee.components.logparser.parser.MessageLogParser;
import biz.bidi.archivee.test.commons.FileReaderUtilsTest;

/**
 * Class test for LogParser component
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 11, 2012
 */
public class MessageLogParserLab {
	
	private String logFile;
	
	public void run() {
		try {
			ArchiveeProperties.loadProperties(this);
			
			System.out.println("Analysing log file: " + logFile);
			
			FileReaderUtilsTest fileReader = new FileReaderUtilsTest(new File(logFile));
			
			DateLevelLogParser dateLevelParser = new DateLevelLogParser();
			MessageLogParser messageLogParser = new MessageLogParser();
			PatternDAO patternDAO = new PatternDAO();
			
//			ILogSender logSender = ListenerManager.getInstance().getLogSender(); 
			
			Long currentTime = System.currentTimeMillis();
			
			int messages = 0;
			
			String line = "";
			while(fileReader.hasNext()) {
				line = fileReader.readLine();
				
				ParserMessage message = new ParserMessage();
				message.setName("archiveeAppTest");
				message.setMessage(line);
				
				try {
					dateLevelParser.parseLog(message);
					
					if(message.getDate() != null && !message.getDate().isEmpty()) {
						//logSender.sendLogMessage(message);
						messageLogParser.parseLog(message);
						
						messages++;
						System.out.println(("#" + messages + " - " +(System.currentTimeMillis() - currentTime)/1000) + "secs - " + line);
						System.out.println(ArchiveePatternUtils.convertToSimpleRegex(line));
						
//						try {
//							Thread.sleep(500);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						
						if(messages > 300) {
							break;
						}
					} else {
						System.out.println("===> SKIPPED: " + line);
					}
				} catch (ArchiveeException e) {
					System.out.println("===> SKIPPED: " + line);
				}
			}

			System.out.println("Elapssed time: " + ((System.currentTimeMillis() - currentTime)/1000) + "secs.");
			
			
			for(Pattern pattern : patternDAO.find(new Pattern())) {
				System.out.println("Pattern Tree data:\n" + pattern.getTreeStringData());
			}
		} catch (ArchiveeException e) {
			ArchiveeException.log(e, "Generic error", this);
		}
		
	}
	
	public static void main(String[] args) {
		new MessageLogParserLab().run();  
	}

	/**
	 * @return the logFile
	 */
	public String getLogFile() {
		return logFile;
	}

	/**
	 * @param logFile the logFile to set
	 */
	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}
	
}