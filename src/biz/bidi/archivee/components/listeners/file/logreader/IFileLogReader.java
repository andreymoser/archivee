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

import biz.bidi.archivee.commons.ArchiveeException;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 4, 2012
 */
public interface IFileLogReader {
	
	/**
	 * Run the process
	 */
	public void run() throws ArchiveeException;
	
	/**
	 * Reads and send the read lines
	 */
	public void readSendLogs() throws ArchiveeException;
	
	/**
	 * Opens the log file
	 */
	public void openLogFile() throws ArchiveeException;
	
	/**
	 * Verify if file has been moved
	 */
	public void verifyFileMoved() throws ArchiveeException;
	
	/**
	 * Closes the log file
	 * @throws ArchiveeException
	 */
	public void closeLogFile() throws ArchiveeException;
	
	/**
	 * Send the read line to DAO
	 * @param line
	 * @throws ArchiveeException
	 */
	public void sendLine(String line) throws ArchiveeException;
	
}
