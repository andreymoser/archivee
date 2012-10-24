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

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.factories.ArchiveeFactoryManager;
import biz.bidi.archivee.commons.factories.IArchiveeFactory;
import biz.bidi.archivee.commons.factories.IArchiveeFactoryManager;
import biz.bidi.archivee.commons.interfaces.ILogParser;
import biz.bidi.archivee.components.logparser.parser.LogParserFactory;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 6, 2012
 */
@SuppressWarnings("rawtypes")
public abstract class LogParserFactoryManager extends ArchiveeFactoryManager implements IArchiveeFactoryManager {

	protected static IArchiveeFactoryManager factoryManager;

	protected static IArchiveeFactory logParserFactory;
	
	public LogParserFactoryManager() {
		super();
		logParserFactory = new LogParserFactory();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.factories.IArchiveeFactoryManager#getFactoryInstance(java.lang.Class, java.lang.Object)
	 */
	@Override
	public IArchiveeFactory getFactoryInstance(Class interfaceClass, Class classObject) throws ArchiveeException {
		IArchiveeFactory factory = super.getFactoryInstance(interfaceClass,classObject); 
		
		if(interfaceClass == ILogParser.class) {
			factory = logParserFactory; 
		}
		
		validateFactory(factory, interfaceClass);
		
		return factory;
	}
	
	@SuppressWarnings("unchecked")
	public ILogParser getLogParser() throws ArchiveeException {
		ILogParser logParser = null;
		IArchiveeFactory<ILogParser, Object> factory = factoryManager.getFactoryInstance(ILogParser.class);
		logParser = factory.createInstance(null);
		return logParser;
	}

}
