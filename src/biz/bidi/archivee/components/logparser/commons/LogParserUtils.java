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

import com.google.code.morphia.query.Query;

import biz.bidi.archivee.commons.dao.IArchiveeGenericDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.factories.IArchiveeFactory;
import biz.bidi.archivee.commons.factories.IArchiveeGenericFactoryManager;
import biz.bidi.archivee.components.logparser.ILogParser;
import biz.bidi.archivee.components.logparser.dao.PatternDAOFactory;
import biz.bidi.archivee.components.logparser.model.Pattern;

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
}
