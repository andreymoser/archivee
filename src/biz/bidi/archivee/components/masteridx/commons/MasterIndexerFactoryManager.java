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
package biz.bidi.archivee.components.masteridx.commons;

import biz.bidi.archivee.commons.dao.IArchiveeGenericDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.factories.ArchiveeGenericFactoryManager;
import biz.bidi.archivee.commons.factories.DaoFactoryManager;
import biz.bidi.archivee.commons.factories.IArchiveeFactory;
import biz.bidi.archivee.commons.factories.IArchiveeGenericFactoryManager;
import biz.bidi.archivee.components.masteridx.indexer.IMasterIndexer;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 28, 2012
 */
@SuppressWarnings("rawtypes")
public class MasterIndexerFactoryManager extends ArchiveeGenericFactoryManager {

	protected static IArchiveeFactory masterIndexerFactory;
	protected static IArchiveeGenericFactoryManager daoFactoryManager;
	
	static {
		instance = new MasterIndexerFactoryManager();
		
		daoFactoryManager = new DaoFactoryManager();
		masterIndexerFactory = new MasterIndexerFactory();
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
		
		if(interfaceClass == IMasterIndexer.class) {
			factory = masterIndexerFactory; 
		}
		if(interfaceClass == IArchiveeGenericDAO.class) {
			factory = daoFactoryManager.getFactoryInstance(interfaceClass, classObject);
		}
		
		validateFactory(factory, interfaceClass);
		
		return factory;
	}
}
