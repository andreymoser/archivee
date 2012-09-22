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
package biz.bidi.archivee.components.logparser.commons;

import biz.bidi.archivee.commons.dao.IArchiveeGenericDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.factories.ArchiveeGenericFactoryManager;
import biz.bidi.archivee.commons.factories.IArchiveeFactory;
import biz.bidi.archivee.commons.model.LogQueue;
import biz.bidi.archivee.commons.model.Pattern;
import biz.bidi.archivee.components.listeners.logsender.ILogSender;
import biz.bidi.archivee.components.listeners.logsender.LogSenderFactory;
import biz.bidi.archivee.components.logparser.ILogParser;
import biz.bidi.archivee.components.logparser.LogParserFactory;
import biz.bidi.archivee.components.logparser.dao.LogQueueDAOFactory;
import biz.bidi.archivee.components.logparser.dao.PatternDAOFactory;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 6, 2012
 */
@SuppressWarnings("rawtypes")
public class LogParserFactoryManager extends ArchiveeGenericFactoryManager {

	protected static IArchiveeFactory logParserFactory;
	protected static IArchiveeFactory patternDAOFactory;
	protected static IArchiveeFactory logQueueDAOFactory;
	protected static IArchiveeFactory logSenderFactory;
	
	static {
		instance = new LogParserFactoryManager();
		
		logParserFactory = new LogParserFactory(); 
		patternDAOFactory = new PatternDAOFactory(); 
		logQueueDAOFactory = new LogQueueDAOFactory(); 
		logSenderFactory = new LogSenderFactory(); 
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
	 * @see biz.bidi.archivee.commons.factories.IArchiveeGenericFactoryManager#getFactoryInstance(java.lang.Class, java.lang.Object)
	 */
	@Override
	public IArchiveeFactory getFactoryInstance(Class interfaceClass, Class classObject) throws ArchiveeException {
		IArchiveeFactory factory = null;
		
		if(interfaceClass == ILogParser.class) {
			factory = logParserFactory; 
		}
		if(interfaceClass == IArchiveeGenericDAO.class && classObject != null) {
			if(classObject == Pattern.class) {
				factory = patternDAOFactory;
			}
			if(classObject == LogQueue.class) {
				factory = logQueueDAOFactory;
			}
		}
		if(interfaceClass == ILogSender.class) {
			factory = logSenderFactory; 
		}
		
		validateFactory(factory, interfaceClass);
		
		return factory;
	}
}
