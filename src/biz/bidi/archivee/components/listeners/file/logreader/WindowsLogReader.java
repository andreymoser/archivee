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

import java.nio.ByteBuffer;

import biz.bidi.archivee.commons.ArchiveeConstants;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.utils.ArchiveeByteBufferUtils;
import biz.bidi.archivee.components.listeners.file.FileListenerThread;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 4, 2012
 */

//TODO test it more
public class WindowsLogReader extends GenericLogReader {

	private static Kernel32 win32API = Kernel32.INSTANCE;	
	private HANDLE handleFile = null;
	private ByteBuffer byteBuffer = ByteBuffer.allocateDirect(ArchiveeConstants.DEFAULT_BUFFER_SIZE);
	private IntByReference bytesRead = new IntByReference(0);
	private int index = 0;
	private ArchiveeByteBufferUtils byteBufferUtils;

	/**
	 * @param fileListener
	 * @param logFile 
	 */
	public WindowsLogReader(FileListenerThread fileListenerThread) {
		super(fileListenerThread);
		byteBufferUtils = new ArchiveeByteBufferUtils(byteBuffer);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.IFileLogReader#openLogFile()
	 */
	@Override
	public void openLogFile() throws ArchiveeException {
		handleFile = win32API.CreateFile(logFile.getAbsolutePath(), com.sun.jna.platform.win32.WinNT.GENERIC_READ, 
				com.sun.jna.platform.win32.WinNT.FILE_SHARE_READ | com.sun.jna.platform.win32.WinNT.FILE_SHARE_WRITE | com.sun.jna.platform.win32.WinNT.FILE_SHARE_DELETE,
				null, com.sun.jna.platform.win32.WinNT.OPEN_EXISTING, 0, null);
		
		closedFile = false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.IFileLogReader#readSendLogs()
	 */
	@Override
	public void readSendLogs() throws ArchiveeException {
		if(win32API.ReadFile(handleFile,byteBuffer,ArchiveeConstants.DEFAULT_BUFFER_SIZE,bytesRead,null) && bytesRead.getValue()>0)
		{
			while(byteBuffer.hasRemaining() && index<bytesRead.getValue())
			{
				sendLine(byteBufferUtils.getNextLine());
			}

			byteBuffer.clear();
			index = 0;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.IFileLogReader#closeLogFile()
	 */
	@Override
	public void closeLogFile() throws ArchiveeException {
		win32API.CloseHandle(handleFile);
		closedFile = true;
		lastLength = 0;
		fileMoved = false;
	}

}
