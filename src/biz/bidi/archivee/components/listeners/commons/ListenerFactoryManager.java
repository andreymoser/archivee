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
import biz.bidi.archivee.commons.factories.ArchiveeFactoryManager;
import biz.bidi.archivee.commons.factories.IArchiveeFactory;
import biz.bidi.archivee.commons.factories.IArchiveeFactoryManager;
import biz.bidi.archivee.commons.interfaces.IFileLogReader;
import biz.bidi.archivee.commons.interfaces.ILogParser;
import biz.bidi.archivee.components.listeners.file.FileListenerThread;
import biz.bidi.archivee.components.listeners.file.IFileListener;
import biz.bidi.archivee.components.listeners.file.logreader.LogReaderFactory;
import biz.bidi.archivee.components.listeners.parser.LogParserFactory;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 6, 2012
 */
@SuppressWarnings("rawtypes")
public abstract class ListenerFactoryManager extends ArchiveeFactoryManager implements IArchiveeFactoryManager {

	protected static IArchiveeFactoryManager factoryManager;

	protected static IArchiveeFactory listenerFactory;
	protected static IArchiveeFactory fileLogReaderFactory;
	protected static IArchiveeFactory logParserFactory;
	
	/**
	 * @param instance
	 */
	public ListenerFactoryManager() {
		super(false,true);
		listenerFactory = new ListenerFactory(); 
		fileLogReaderFactory = new LogReaderFactory();
		logParserFactory = new LogParserFactory();
		
		instance = this;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.factories.IArchiveeFactoryManager#getFactoryInstance(java.lang.Class, java.lang.Object)
	 */
	@Override
	public IArchiveeFactory getFactoryInstance(Class interfaceClass, Class classObject) throws ArchiveeException {
		IArchiveeFactory factory = super.getFactoryInstance(interfaceClass,classObject); 
		
		if(interfaceClass == IFileListener.class) {
			factory = listenerFactory; 
		}
		if(classObject == IFileLogReader.class) {
			factory = fileLogReaderFactory; 
		}
		if(classObject == ILogParser.class) {
			factory = logParserFactory; 
		}
		
		validateFactory(factory, interfaceClass);
		
		return factory;
	}

	@SuppressWarnings("unchecked")
	public IFileLogReader getFileLogReader(FileListenerThread fileListenerThread) throws ArchiveeException {
		IFileLogReader fileLogReader = null;
		IArchiveeFactory<IFileLogReader, FileListenerThread> factory = factoryManager.getFactoryInstance(IFileLogReader.class);
		fileLogReader = factory.createInstance(fileListenerThread);
		return fileLogReader;
	}
	
	@SuppressWarnings("unchecked")
	public ILogParser getDateLevelLogParser() throws ArchiveeException {
		ILogParser logSender = null;
		IArchiveeFactory<ILogParser, Object> factory = factoryManager.getFactoryInstance(ILogParser.class);
		logSender = factory.createInstance(null);
		return logSender;
	}
	
	@SuppressWarnings("unchecked")
	public IFileListener getFileListener() throws ArchiveeException {
		IFileListener fileListener = null;
		IArchiveeFactory<IFileListener, Object> factory = factoryManager.getFactoryInstance(IFileListener.class);
		fileListener = factory.createInstance(null);
		return fileListener;
	}
}
