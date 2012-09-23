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
import biz.bidi.archivee.commons.factories.ArchiveeGenericFactoryManager;
import biz.bidi.archivee.commons.factories.IArchiveeFactory;
import biz.bidi.archivee.commons.interfaces.ILogParser;
import biz.bidi.archivee.components.listeners.file.logreader.LogReaderFactory;
import biz.bidi.archivee.components.listeners.file.logreader.IFileLogReader;
import biz.bidi.archivee.components.listeners.logsender.ILogSender;
import biz.bidi.archivee.components.listeners.logsender.LogSenderFactory;
import biz.bidi.archivee.components.listeners.parser.LogParserFactory;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 6, 2012
 */
@SuppressWarnings("rawtypes")
public class ListenerFactoryManager extends ArchiveeGenericFactoryManager {

	protected static IArchiveeFactory fileLogReaderFactory;
	protected static IArchiveeFactory logSenderFactory;
	protected static IArchiveeFactory logParserFactory;
	
	static {
		instance = new ListenerFactoryManager();
		
		fileLogReaderFactory = new LogReaderFactory(); 
		logSenderFactory = new LogSenderFactory(); 
		logParserFactory = new LogParserFactory(); 
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.IArchiveeSingleTonFactoryManager#getFactoryInstance(java.lang.Class)
	 */
	@Override
	public IArchiveeFactory getFactoryInstance(Class classObject) throws ArchiveeException {
		return getFactoryInstance(classObject, null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.factories.IArchiveeGenericFactoryManager#getFactoryInstance(java.lang.Class, java.lang.Class)
	 */
	@Override
	public IArchiveeFactory getFactoryInstance(Class interfaceObject, Class classObject) throws ArchiveeException {
		IArchiveeFactory factory = null;
		
		if(classObject == IFileLogReader.class) {
			factory = fileLogReaderFactory; 
		}
		if(classObject == ILogSender.class) {
			factory = logSenderFactory; 
		}
		if(classObject == ILogParser.class) {
			factory = logParserFactory; 
		}
		
		validateFactory(factory, classObject);
		
		return factory;
	}
	
}
