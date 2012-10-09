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

import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;

import biz.bidi.archivee.commons.dao.IArchiveeGenericDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.interfaces.ICompressorSender;
import biz.bidi.archivee.commons.interfaces.ILogSender;
import biz.bidi.archivee.commons.interfaces.IPatternSender;
import biz.bidi.archivee.commons.jms.IArchiveeMessaging;
import biz.bidi.archivee.commons.model.mongodb.App;
import biz.bidi.archivee.commons.model.mongodb.Context;
import biz.bidi.archivee.commons.model.mongodb.ContextIndex;
import biz.bidi.archivee.commons.model.mongodb.ContextQueue;
import biz.bidi.archivee.commons.model.mongodb.Dictionary;
import biz.bidi.archivee.commons.model.mongodb.DictionaryQueue;
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
public abstract class ArchiveeFactoryManager implements IArchiveeFactoryManager {

	protected static IArchiveeFactoryManager instance;
	
	protected static IArchiveeFactoryManager daoFactoryManager;
	protected static IArchiveeFactoryManager jmsFactoryManager;
	
	public ArchiveeFactoryManager() {
		daoFactoryManager = new DaoFactoryManager(this);
		jmsFactoryManager = new JMSFactoryManager(this);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.factories.IArchiveeFactoryManager#getManagerInstance()
	 */
	@Override
	public IArchiveeFactoryManager getManagerInstance() {
		return instance;
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
		
		if(interfaceClass == IArchiveeGenericDAO.class) {
			factory = daoFactoryManager.getFactoryInstance(interfaceClass, classObject);
			validateFactory(factory, classObject);
		}
		if(interfaceClass == IArchiveeMessaging.class) {
			factory = jmsFactoryManager.getFactoryInstance(interfaceClass, classObject); 
			validateFactory(factory, classObject);
		}
		
		return factory;
	}
	
	@SuppressWarnings("rawtypes")
	protected void validateFactory(IArchiveeFactory factory, Class classObject) throws ArchiveeException {
		if(factory == null) {
			throw new ArchiveeException("Invalid factory: null",factory,classObject);
		}
	}
	
	/**
	 *
	 * DAOs instances
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	public IArchiveeGenericDAO<Pattern, Query<Pattern>, Key<Pattern>> getPatternDAO() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = instance.getFactoryInstance(IArchiveeGenericDAO.class,Pattern.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}
	
	@SuppressWarnings("unchecked")
	public IArchiveeGenericDAO<LogQueue, Query<LogQueue>, Key<LogQueue>> getLoqQueue() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = instance.getFactoryInstance(IArchiveeGenericDAO.class,LogQueue.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}
	
	@SuppressWarnings("unchecked")
	public IArchiveeGenericDAO<App, Query<App>, Key<App>> getApp() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = instance.getFactoryInstance(IArchiveeGenericDAO.class,App.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}
	
	@SuppressWarnings("unchecked")
	public IArchiveeGenericDAO<Context, Query<Context>, Key<Context>> getContext() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = instance.getFactoryInstance(IArchiveeGenericDAO.class,Context.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}

	@SuppressWarnings("unchecked")
	public IArchiveeGenericDAO<ContextIndex, Query<ContextIndex>, Key<ContextIndex>> getContextIndex() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = instance.getFactoryInstance(IArchiveeGenericDAO.class,ContextIndex.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}

	@SuppressWarnings("unchecked")
	public IArchiveeGenericDAO<ContextQueue, Query<ContextQueue>, Key<ContextQueue>> getContextQueue() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = instance.getFactoryInstance(IArchiveeGenericDAO.class,ContextQueue.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}

	@SuppressWarnings("unchecked")
	public IArchiveeGenericDAO<Dictionary, Query<Dictionary>, Key<Dictionary>> getDictionary() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = instance.getFactoryInstance(IArchiveeGenericDAO.class,Dictionary.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}
	
	@SuppressWarnings("unchecked")
	public IArchiveeGenericDAO<DictionaryQueue, Query<DictionaryQueue>, Key<DictionaryQueue>> getDictionaryQueue() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = instance.getFactoryInstance(IArchiveeGenericDAO.class,DictionaryQueue.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}

	@SuppressWarnings("unchecked")
	public IArchiveeGenericDAO<MasterIndex, Query<MasterIndex>, Key<MasterIndex>> getMasterIndex() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = instance.getFactoryInstance(IArchiveeGenericDAO.class,MasterIndex.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}

	@SuppressWarnings("unchecked")
	public IArchiveeGenericDAO<Template, Query<Template>, Key<Template>> getTemplate() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = instance.getFactoryInstance(IArchiveeGenericDAO.class,Template.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}
	
	@SuppressWarnings("unchecked")
	public IArchiveeGenericDAO<TemplateDictionary, Query<TemplateDictionary>, Key<TemplateDictionary>> getTemplateDictionary() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = instance.getFactoryInstance(IArchiveeGenericDAO.class,TemplateDictionary.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}
	
	/**
	 *
	 * JMS instances
	 * 
	 */

	/**
	 * @return
	 * @throws ArchiveeException 
	 */
	@SuppressWarnings("unchecked")
	public ILogSender getLogSender() throws ArchiveeException {
		ILogSender logSender = null;
		IArchiveeFactory<ILogSender, Object> factory = instance.getFactoryInstance(IArchiveeMessaging.class,ILogSender.class);
		logSender = factory.createInstance(null);
		return logSender;
	}
	
	@SuppressWarnings("unchecked")
	public IPatternSender getPatternSender() throws ArchiveeException {
		IPatternSender patternSender = null;
		IArchiveeFactory<IPatternSender, Object> factory = instance.getFactoryInstance(IArchiveeMessaging.class,IPatternSender.class);
		patternSender = factory.createInstance(null);
		return patternSender;
	}
	
	@SuppressWarnings("unchecked")
	public ICompressorSender getCompressorSender() throws ArchiveeException {
		ICompressorSender compressorSender = null;
		IArchiveeFactory<ICompressorSender, Object> factory = instance.getFactoryInstance(IArchiveeMessaging.class,ICompressorSender.class);
		compressorSender = factory.createInstance(null);
		return compressorSender;
	}
}