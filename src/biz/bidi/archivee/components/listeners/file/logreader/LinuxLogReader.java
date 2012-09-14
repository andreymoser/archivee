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
package biz.bidi.archivee.components.listeners.file.logreader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import biz.bidi.archivee.commons.ArchiveeConstants;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.components.listeners.file.FileListenerThread;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 4, 2012
 */
public class LinuxLogReader extends GenericLogReader {

	private FileInputStream fis = null;
	
	/**
	 * @param fileListener
	 */
	public LinuxLogReader(FileListenerThread fileListenerThread) {
		super(fileListenerThread);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.components.listeners.file.logreader.IFileLogReader#openLogFile()
	 */
	@Override
	public void openLogFile() throws ArchiveeException {
		try {
			fis = new FileInputStream(logFile);
			closedFile = false;
		} catch (FileNotFoundException e) {
			throw new ArchiveeException("Log file not found",e,logFile,this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.components.listeners.file.logreader.IFileLogReader#readSendLogs()
	 */
	@Override
	public void readSendLogs() throws ArchiveeException {
		try {
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			while ((logLine = br.readLine()) != null)
			{
				lastLength += logLine.getBytes("UTF-8").length;
				lastLength += ArchiveeConstants.LINE_SEPARATOR.length();
				logLine += ArchiveeConstants.LINE_SEPARATOR;
				
				sendLine(logLine);
			}
		} catch (UnsupportedEncodingException e) {
			throw new ArchiveeException("Unsupported encoding format for log file",e,logFile,this);					
		} catch (IOException e) {
			throw new ArchiveeException("Error while reading and sending log file",e,logFile,this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.components.listeners.file.logreader.IFileLogReader#closeLogFile()
	 */
	@Override
	public void closeLogFile() throws ArchiveeException {
		try {
			fis.close();
			closedFile = true;
			lastLength = 0;
			fileMoved = false;
		} catch (IOException e) {
			throw new ArchiveeException("Error while closing log file",e,logFile,this);
		}
	}
	
}