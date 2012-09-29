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
import biz.bidi.archivee.commons.factories.IArchiveeFactory;
import biz.bidi.archivee.commons.factories.IArchiveeGenericFactoryManager;
import biz.bidi.archivee.commons.interfaces.ILogParser;
import biz.bidi.archivee.commons.model.mongodb.LogQueue;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.components.listeners.logsender.ILogSender;
import biz.bidi.archivee.components.masteridx.indexer.IMasterIndexer;

import com.google.code.morphia.query.Query;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 28, 2012
 */
public class MasterIndexerUtils {
	
	/**
	 * The file listener manager factory
	 */
	public static final IArchiveeGenericFactoryManager masterIndexerManager = 
			new MasterIndexerFactoryManager();
	
	@SuppressWarnings("rawtypes")
	public static IArchiveeFactory getFactory(Class interfaceClass) throws ArchiveeException {
		return masterIndexerManager.getFactoryInstance(interfaceClass);
	}
	
	@SuppressWarnings("rawtypes")
	public static IArchiveeFactory getFactory(Class interfaceClass, Class classObject) throws ArchiveeException {
		return masterIndexerManager.getFactoryInstance(interfaceClass, classObject);
	}
	
	/**
	 * @return
	 * @throws ArchiveeException 
	 */
	@SuppressWarnings("unchecked")
	public static IMasterIndexer getMasterIndexer() throws ArchiveeException {
		IMasterIndexer masterIndexer = null;
		IArchiveeFactory<IMasterIndexer, Object> factory = getFactory(IMasterIndexer.class);
		masterIndexer = factory.createInstance(null);
		return masterIndexer;
	}
}
