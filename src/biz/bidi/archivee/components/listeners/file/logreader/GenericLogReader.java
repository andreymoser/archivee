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

import java.io.File;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.interfaces.IFileLogReader;
import biz.bidi.archivee.commons.interfaces.ILogParser;
import biz.bidi.archivee.commons.interfaces.ILogSender;
import biz.bidi.archivee.commons.model.xml.ParserMessage;
import biz.bidi.archivee.components.listeners.commons.ListenerManager;
import biz.bidi.archivee.components.listeners.file.FileListener;
import biz.bidi.archivee.components.listeners.file.FileListenerThread;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 4, 2012
 */
public abstract class GenericLogReader implements IFileLogReader {

	/**
	 * The file listener
	 */
	protected FileListener fileListener;
	/**
	 * The log file
	 */
	protected File logFile;
	/*
	 * The read log line
	 */
	protected String logLine;
	/**
	 * The log sender
	 */
	protected ILogSender logSender;
	
	protected long lastLength = 0;
    protected boolean closedFile = true;
    protected boolean fileMoved = false;
    protected long lastModifiedTime = 0;

	/**
	 * @param fileListener
	 * @param logFile 
	 */
	public GenericLogReader(FileListenerThread fileListenerThread) {
		super();
		this.fileListener = fileListenerThread.getFileListener();
		this.logFile = fileListenerThread.getLogFile();
	}


	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.IFileLogReader#run()
	 */
	@Override
	public void run() throws ArchiveeException {
		try {
			this.logSender = ListenerManager.getInstance().getLogSender();
			
			while(true)
			{
				if(closedFile)
				{
					openLogFile();
				}
				
				readSendLogs();
	
				if(fileMoved) {
					closeLogFile();
				} else {
					verifyFileMoved();
				}
			}	
		} catch (ArchiveeException e) {
			e.error("Generic error",e,this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.IFileLogReader#verifyFileMoved()
	 */
	@Override
	public void verifyFileMoved() throws ArchiveeException {
		try {
			/**
			 * The java.io.File doesn't lock the file when we look for file attributes
			 */
			lastModifiedTime = logFile.lastModified();
			
			while(true)
			{
				Thread.sleep(1000);
				
				if(logFile.length()<lastLength)
				{
					fileMoved = true;
					break;
				}
				
				if(lastModifiedTime != logFile.lastModified() || logFile.length()>lastLength)
					break;
			}
		} catch (Exception e) {
			throw new ArchiveeException(e,"File attribute error",this,logFile,lastModifiedTime);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.IFileLogReader#sendLine(java.lang.String)
	 */
	@Override
	public void sendLine(String line) throws ArchiveeException {
		ParserMessage message = new ParserMessage();
		message.setMessage(line);
		
		ILogParser parser = ListenerManager.getInstance().getDateLevelLogParser(); 
		parser.parseLog(message);
		
		if(message.getDate() != null && !message.getDate().isEmpty()) {
			this.logSender.sendLogMessage(message);
		}
	}
	
	/**
	 * @return the fileListener
	 */
	public FileListener getFileListener() {
		return fileListener;
	}

	/**
	 * @param fileListener the fileListener to set
	 */
	public void setFileListener(FileListener fileListener) {
		this.fileListener = fileListener;
	}

	/**
	 * @return the logFile
	 */
	public File getLogFile() {
		return logFile;
	}

	/**
	 * @param logFile the logFile to set
	 */
	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}
	
}
