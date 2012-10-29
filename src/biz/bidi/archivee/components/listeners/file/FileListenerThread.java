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
package biz.bidi.archivee.components.listeners.file;

import java.io.File;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.components.listeners.commons.ListenerManager;
import biz.bidi.archivee.components.listeners.file.logreader.IFileLogReader;

/**
 * The file listener thread
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 4, 2012
 */
public class FileListenerThread implements Runnable {

	/**
	 * The main thread file listener
	 */
	private FileListener fileListener;
	/**
	 * The log file to be listened
	 */
	private File logFile;	
	
	/**
	 * @param fileListener
	 */
	public FileListenerThread(FileListener fileListener, File logFile) {
		super();
		this.fileListener = fileListener;
		this.logFile = logFile;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		IFileLogReader logReader = null;
		try {
			logReader = ListenerManager.getInstance().getFileLogReader(this);
			logReader.run();
		} catch (ArchiveeException e) {
			e.error("Error while processing log file thread", this, logReader, fileListener);
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
