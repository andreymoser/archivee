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
import biz.bidi.archivee.commons.dao.mongodb.factories.AppDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.ContextDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.ContextIndexDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.ContextQueueDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.DictionaryDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.DictionaryQueueDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.LockerDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.LogQueueDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.MasterIndexDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.PatternDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.TemplateDAOFactory;
import biz.bidi.archivee.commons.dao.mongodb.factories.TemplateDictionaryDAOFactory;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.mongodb.App;
import biz.bidi.archivee.commons.model.mongodb.Context;
import biz.bidi.archivee.commons.model.mongodb.ContextIndex;
import biz.bidi.archivee.commons.model.mongodb.ContextQueue;
import biz.bidi.archivee.commons.model.mongodb.Dictionary;
import biz.bidi.archivee.commons.model.mongodb.DictionaryQueue;
import biz.bidi.archivee.commons.model.mongodb.Locker;
import biz.bidi.archivee.commons.model.mongodb.LogQueue;
import biz.bidi.archivee.commons.model.mongodb.MasterIndex;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.commons.model.mongodb.Template;
import biz.bidi.archivee.commons.model.mongodb.TemplateDictionary;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 6, 2012
 */
@SuppressWarnings("rawtypes")
public class DaoFactoryManager implements IArchiveeFactoryManager {

	protected IArchiveeFactoryManager archiveeFactoryManager;
	
	protected IArchiveeFactory patternDAOFactory;
	protected IArchiveeFactory logQueueDAOFactory;
	protected IArchiveeFactory appDAOFactory;
	protected IArchiveeFactory contextDAOFactory;
	protected IArchiveeFactory contextQueueDAOFactory;
	protected IArchiveeFactory contextIndexDAOFactory;
	protected IArchiveeFactory dictionaryDAOFactory;
	protected IArchiveeFactory dictionaryQueueDAOFactory;
	protected IArchiveeFactory masterIndexDAOFactory;
	protected IArchiveeFactory templateDAOFactory;
	protected IArchiveeFactory templateDictionaryDAOFactory;
	protected IArchiveeFactory lockerDAOFactory;
	
	public DaoFactoryManager(IArchiveeFactoryManager archiveeFactoryManager) {
		this.archiveeFactoryManager = archiveeFactoryManager;
		
		patternDAOFactory = new PatternDAOFactory(); 
		logQueueDAOFactory = new LogQueueDAOFactory(); 
		appDAOFactory = new AppDAOFactory(); 
		contextDAOFactory = new ContextDAOFactory(); 
		contextQueueDAOFactory = new ContextQueueDAOFactory(); 
		contextIndexDAOFactory = new ContextIndexDAOFactory(); 
		dictionaryDAOFactory = new DictionaryDAOFactory(); 
		dictionaryQueueDAOFactory = new DictionaryQueueDAOFactory(); 
		masterIndexDAOFactory = new MasterIndexDAOFactory(); 
		templateDAOFactory = new TemplateDAOFactory();
		templateDictionaryDAOFactory = new TemplateDictionaryDAOFactory();
		lockerDAOFactory = new LockerDAOFactory();
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
	 * @see biz.bidi.archivee.commons.factories.IArchiveeFactoryManager#getFactoryInstance(java.lang.Class, java.lang.Object)
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
			if(classObject == App.class) {
				factory = appDAOFactory;
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
			if(classObject == TemplateDictionary.class) {
				factory = templateDictionaryDAOFactory;
			}
			if(classObject == Locker.class) {
				factory = lockerDAOFactory;
			}
		}
		
		return factory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.factories.IArchiveeFactoryManager#getManagerInstance()
	 */
	@Override
	public IArchiveeFactoryManager getManagerInstance() {
		return archiveeFactoryManager;
	}
}
