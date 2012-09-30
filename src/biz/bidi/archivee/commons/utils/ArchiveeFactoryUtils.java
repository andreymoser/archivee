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
package biz.bidi.archivee.commons.utils;

import biz.bidi.archivee.commons.dao.IArchiveeGenericDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.factories.IArchiveeFactory;
import biz.bidi.archivee.commons.factories.IArchiveeFactoryManager;
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

import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 29, 2012
 */
@SuppressWarnings(value="rawtypes")
public abstract class ArchiveeFactoryUtils {

	/**
	 * The file listener manager factory
	 */
	protected static IArchiveeFactoryManager factoryManagerInstance;
	
	public static IArchiveeFactory getFactory(Class interfaceClass) throws ArchiveeException {
		return factoryManagerInstance.getFactoryInstance(interfaceClass);
	}
	
	public static IArchiveeFactory getFactory(Class interfaceClass, Class classObject) throws ArchiveeException {
		return factoryManagerInstance.getFactoryInstance(interfaceClass, classObject);
	}
	
	/**
	 *
	 * DAOs instances
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	public static IArchiveeGenericDAO<Pattern, Query<Pattern>, Key<Pattern>> getPatternDAO() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = getFactory(IArchiveeGenericDAO.class,Pattern.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}
	
	@SuppressWarnings("unchecked")
	public static IArchiveeGenericDAO<LogQueue, Query<LogQueue>, Key<LogQueue>> getLoqQueue() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = getFactory(IArchiveeGenericDAO.class,LogQueue.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}
	
	@SuppressWarnings("unchecked")
	public static IArchiveeGenericDAO<App, Query<App>, Key<App>> getApp() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = getFactory(IArchiveeGenericDAO.class,App.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}
	
	@SuppressWarnings("unchecked")
	public static IArchiveeGenericDAO<Context, Query<Context>, Key<Context>> getContext() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = getFactory(IArchiveeGenericDAO.class,Context.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}

	@SuppressWarnings("unchecked")
	public static IArchiveeGenericDAO<ContextIndex, Query<ContextIndex>, Key<ContextIndex>> getContextIndex() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = getFactory(IArchiveeGenericDAO.class,ContextIndex.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}

	@SuppressWarnings("unchecked")
	public static IArchiveeGenericDAO<ContextQueue, Query<ContextQueue>, Key<ContextQueue>> getContextQueue() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = getFactory(IArchiveeGenericDAO.class,ContextQueue.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}

	@SuppressWarnings("unchecked")
	public static IArchiveeGenericDAO<Dictionary, Query<Dictionary>, Key<Dictionary>> getDictionary() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = getFactory(IArchiveeGenericDAO.class,Dictionary.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}
	
	@SuppressWarnings("unchecked")
	public static IArchiveeGenericDAO<DictionaryQueue, Query<DictionaryQueue>, Key<DictionaryQueue>> getDictionaryQueue() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = getFactory(IArchiveeGenericDAO.class,DictionaryQueue.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}

	@SuppressWarnings("unchecked")
	public static IArchiveeGenericDAO<MasterIndex, Query<MasterIndex>, Key<MasterIndex>> getMasterIndex() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = getFactory(IArchiveeGenericDAO.class,MasterIndex.class);
		daoInstance = factory.createInstance(null);
		return daoInstance;
	}

	@SuppressWarnings("unchecked")
	public static IArchiveeGenericDAO<Template, Query<Template>, Key<Template>> getTemplate() throws ArchiveeException {
		IArchiveeGenericDAO daoInstance = null;
		IArchiveeFactory<IArchiveeGenericDAO, Object> factory = getFactory(IArchiveeGenericDAO.class,Template.class);
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
	public static ILogSender getLogSender() throws ArchiveeException {
		ILogSender logSender = null;
		IArchiveeFactory<ILogSender, Object> factory = getFactory(IArchiveeMessaging.class,ILogSender.class);
		logSender = factory.createInstance(null);
		return logSender;
	}
	
	@SuppressWarnings("unchecked")
	public static IPatternSender getPatternSender() throws ArchiveeException {
		IPatternSender patternSender = null;
		IArchiveeFactory<IPatternSender, Object> factory = getFactory(IArchiveeMessaging.class,IPatternSender.class);
		patternSender = factory.createInstance(null);
		return patternSender;
	}

}
