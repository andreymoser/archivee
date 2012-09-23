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
import biz.bidi.archivee.commons.factories.IArchiveeFactory;
import biz.bidi.archivee.commons.factories.IArchiveeGenericFactoryManager;
import biz.bidi.archivee.commons.interfaces.ILogParser;
import biz.bidi.archivee.commons.model.mongodb.LogQueue;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.components.listeners.logsender.ILogSender;

import com.google.code.morphia.query.Query;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 6, 2012
 */
public class LogParserUtils {
	
	/**
	 * The file listener manager factory
	 */
	public static final IArchiveeGenericFactoryManager logParserManager = 
			new LogParserFactoryManager();
	
	@SuppressWarnings("rawtypes")
	public static IArchiveeFactory getFactory(Class interfaceClass) throws ArchiveeException {
		return logParserManager.getFactoryInstance(interfaceClass);
	}
	
	@SuppressWarnings("rawtypes")
	public static IArchiveeFactory getFactory(Class interfaceClass, Class classObject) throws ArchiveeException {
		return logParserManager.getFactoryInstance(interfaceClass, classObject);
	}
	
	@SuppressWarnings("unchecked")
	public static ILogParser getLogParser() throws ArchiveeException {
		ILogParser logParser = null;
		IArchiveeFactory<ILogParser, Object> factory = getFactory(ILogParser.class);
		logParser = factory.createInstance(null);
		return logParser;
	}
	
	@SuppressWarnings("unchecked")
	public static IArchiveeGenericDAO<Pattern, Query<Pattern>> getPatternDAO() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = getFactory(IArchiveeGenericDAO.class,Pattern.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}
	
	@SuppressWarnings("unchecked")
	public static IArchiveeGenericDAO<LogQueue, Query<LogQueue>> getLoqQueue() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = getFactory(IArchiveeGenericDAO.class,LogQueue.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}

	/**
	 * @return
	 * @throws ArchiveeException 
	 */
	@SuppressWarnings("unchecked")
	public static ILogSender getLogSender() throws ArchiveeException {
		ILogSender logSender = null;
		IArchiveeFactory<ILogSender, Object> factory = getFactory(ILogSender.class);
		logSender = factory.createInstance(null);
		return logSender;
	}
}
