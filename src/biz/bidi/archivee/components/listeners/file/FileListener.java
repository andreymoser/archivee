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
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.properties.ArchiveeProperties;
import biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader;
import biz.bidi.archivee.commons.utils.ArchiveeFileUtils;

/**
 * Main archivee file listener component
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 4, 2012
 */
public class FileListener implements IFileListener, IArchiveePropertiesLoader {

	/**
	 * The logs directory
	 */
	private String logsDirectory;
	/**
	 * The regex for the log filenames to be listened
	 */
	private String regexLogFilename;
	
	/**
	 * Starts process
	 */
	public void start() {
		ArrayList<File> files = null;
		FileListenerThread[] fileListeners = null;
		try {
			files = ArchiveeFileUtils.findFiles(regexLogFilename, logsDirectory);
			fileListeners = new FileListenerThread[files.size()];
			
			ExecutorService executor = Executors.newFixedThreadPool(files.size());
			
			for(int i = 0; i < fileListeners.length; i++) {
				fileListeners[i] = new FileListenerThread(this, files.get(i));
				executor.execute(fileListeners[i]);
			}
			
			System.out.println("Info: Waiting for file listeners threads to terminate...");
			executor.shutdown();
			while(!executor.isTerminated()) {
				Thread.sleep(5000);
			}
			System.out.println("Info: threads terminated!");
		} catch (Exception e) {
			ArchiveeException.error(e, "Error in main file listener thread",this,files,fileListeners);
		}
	}
	
	/**
	 * @return the logsDirectory
	 */
	public String getLogsDirectory() {
		return logsDirectory;
	}

	/**
	 * @param logsDirectory the logsDirectory to set
	 */
	public void setLogsDirectory(String logsDirectory) {
		this.logsDirectory = logsDirectory;
	}

	/**
	 * @return the regexLogFilename
	 */
	public String getRegexLogFilename() {
		return regexLogFilename;
	}

	/**
	 * @param regexLogFilename the regexLogFilename to set
	 */
	public void setRegexLogFilename(String regexLogFilename) {
		this.regexLogFilename = regexLogFilename;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader#loadProperties(java.lang.String)
	 */
	@Override
	public void loadProperties(String prefixKey) throws ArchiveeException {
		ArchiveeProperties.loadProperties(this, prefixKey);
	}
	
}
