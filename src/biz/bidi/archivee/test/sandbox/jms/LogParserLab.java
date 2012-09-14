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

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.properties.ArchiveeProperties;
import biz.bidi.archivee.components.logparser.LogParser;
import biz.bidi.archivee.test.commons.FileReaderUtilsTest;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 11, 2012
 */
public class LogParserLab {
	
	private String logFile;
	
	public void run() {
		try {
			ArchiveeProperties.loadProperties(this);
			
			System.out.println("Analysing log file: " + logFile);
			
			FileReaderUtilsTest fileReader = new FileReaderUtilsTest(new File(logFile));
			
			LogParser logParser = new LogParser();
			String line = "";
			while(fileReader.hasNext()) {
				line = fileReader.readLine();
				logParser.parseLogLine(line);
				System.out.println(line);
			}
		} catch (ArchiveeException e) {
			ArchiveeException.log(e, "Generic error", this);
		}
	}
	
	public static void main(String[] args) {
		new LogParserLab().run();  
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
