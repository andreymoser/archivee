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
package biz.bidi.archivee.commons.factories;

import biz.bidi.archivee.commons.dao.IArchiveeGenericDAO;
import biz.bidi.archivee.commons.dao.mongodb.factories.ContextDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.ContextIndexDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.ContextQueueDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.DictionaryDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.DictionaryQueueDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.LogQueueDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.MasterIndexDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.PatternDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.TemplateDAOFactory;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.mongodb.Context;
import biz.bidi.archivee.commons.model.mongodb.ContextIndex;
import biz.bidi.archivee.commons.model.mongodb.ContextQueue;
import biz.bidi.archivee.commons.model.mongodb.Dictionary;
import biz.bidi.archivee.commons.model.mongodb.DictionaryQueue;
import biz.bidi.archivee.commons.model.mongodb.LogQueue;
import biz.bidi.archivee.commons.model.mongodb.MasterIndex;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.commons.model.mongodb.Template;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 6, 2012
 */
@SuppressWarnings("rawtypes")
public class DaoFactoryManager extends ArchiveeGenericFactoryManager {

	protected static IArchiveeFactory patternDAOFactory;
	protected static IArchiveeFactory logQueueDAOFactory;
	protected static IArchiveeFactory appDAOFactory;
	protected static IArchiveeFactory contextDAOFactory;
	protected static IArchiveeFactory contextQueueDAOFactory;
	protected static IArchiveeFactory contextIndexDAOFactory;
	protected static IArchiveeFactory dictionaryDAOFactory;
	protected static IArchiveeFactory dictionaryQueueDAOFactory;
	protected static IArchiveeFactory masterIndexDAOFactory;
	protected static IArchiveeFactory templateDAOFactory;
	
	static {
		instance = new DaoFactoryManager();
		
		patternDAOFactory = new PatternDAOFactory(); 
		logQueueDAOFactory = new LogQueueDAOFactory(); 
		appDAOFactory = new LogQueueDAOFactory(); 
		contextDAOFactory = new ContextDAOFactory(); 
		contextQueueDAOFactory = new ContextQueueDAOFactory(); 
		contextIndexDAOFactory = new ContextIndexDAOFactory(); 
		dictionaryDAOFactory = new DictionaryDAOFactory(); 
		dictionaryQueueDAOFactory = new DictionaryQueueDAOFactory(); 
		masterIndexDAOFactory = new MasterIndexDAOFactory(); 
		templateDAOFactory = new TemplateDAOFactory(); 
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
		
		if(interfaceClass == IArchiveeGenericDAO.class && classObject != null) {
			if(classObject == Pattern.class) {
				factory = patternDAOFactory;
			}
			if(classObject == LogQueue.class) {
				factory = logQueueDAOFactory;
			}
			if(classObject == Context.class) {
				factory = contextDAOFactory;
			}
			if(classObject == ContextIndex.class) {
				factory = contextIndexDAOFactory;
			}
			if(classObject == ContextQueue.class) {
				factory = contextQueueDAOFactory;
			}
			if(classObject == Dictionary.class) {
				factory = dictionaryDAOFactory;
			}
			if(classObject == DictionaryQueue.class) {
				factory = dictionaryQueueDAOFactory;
			}
			if(classObject == MasterIndex.class) {
				factory = masterIndexDAOFactory;
			}
			if(classObject == Template.class) {
				factory = templateDAOFactory;
			}
		}
		
		validateFactory(factory, interfaceClass);
		
		return factory;
	}
}
