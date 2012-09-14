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
package biz.bidi.archivee.components.listeners.commons;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.factories.IArchiveeFactory;
import biz.bidi.archivee.commons.factories.IArchiveeGenericFactoryManager;
import biz.bidi.archivee.components.listeners.file.FileListener;
import biz.bidi.archivee.components.listeners.file.FileListenerThread;
import biz.bidi.archivee.components.listeners.file.logreader.IFileLogReader;
import biz.bidi.archivee.components.listeners.logsender.ILogSender;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 6, 2012
 */
public class ListenersUtils {
	
	/**
	 * The file listener manager factory
	 */
	public static final IArchiveeGenericFactoryManager fileListenerManager = 
			new ListenerFactoryManager();
	
	@SuppressWarnings("rawtypes")
	public static IArchiveeFactory getFactory(Class classObject) throws ArchiveeException {
		return fileListenerManager.getFactoryInstance(classObject);
	}
	
	@SuppressWarnings("unchecked")
	public static ILogSender getLoSenderInstance() throws ArchiveeException {
		ILogSender logSender = null;
		IArchiveeFactory<ILogSender, Object> factory = getFactory(ILogSender.class);
		logSender = factory.createInstance(null);
		return logSender;
	}
	
	@SuppressWarnings("unchecked")
	public static IFileLogReader getFileLogReader(FileListenerThread fileListenerThread) throws ArchiveeException {
		IFileLogReader fileLogReader = null;
		IArchiveeFactory<IFileLogReader, FileListenerThread> factory = getFactory(IFileLogReader.class);
		fileLogReader = factory.createInstance(fileListenerThread);
		return fileLogReader;
	}
}
