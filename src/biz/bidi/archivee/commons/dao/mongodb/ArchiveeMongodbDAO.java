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
package biz.bidi.archivee.commons.dao.mongodb;

import biz.bidi.archivee.commons.dao.IArchiveeGenericDAO;
import biz.bidi.archivee.commons.dao.mongodb.dao.TemplateDictionaryDAO;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.mongodb.App;
import biz.bidi.archivee.commons.model.mongodb.Context;
import biz.bidi.archivee.commons.model.mongodb.ContextIndex;
import biz.bidi.archivee.commons.model.mongodb.ContextQueue;
import biz.bidi.archivee.commons.model.mongodb.Dictionary;
import biz.bidi.archivee.commons.model.mongodb.DictionaryQueue;
import biz.bidi.archivee.commons.model.mongodb.IEntity;
import biz.bidi.archivee.commons.model.mongodb.LogQueue;
import biz.bidi.archivee.commons.model.mongodb.MasterIndex;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.commons.model.mongodb.Template;
import biz.bidi.archivee.commons.properties.ArchiveeProperties;
import biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 12, 2012
 */
public abstract class ArchiveeMongodbDAO<E extends IEntity> 
	implements IArchiveeGenericDAO<E, Query<E>, Key<E>>, IArchiveePropertiesLoader {

	/*
	 * The mongo db host
	 */
	protected String host;
	/**
	 * The database name
	 */
	protected String database;
	/**
	 * The mongodb connection - Morphia datastore
	 */
	protected Datastore ds;
	
	public ArchiveeMongodbDAO() {
		try {
			loadProperties(ArchiveeMongodbDAO.class.getSimpleName() + ".");
			
			Mongo mongo = new Mongo(host);
			
			/*
			 * Entities should added here
			 */
			Morphia morphia = new Morphia();
			morphia.map(Pattern.class);
			morphia.map(LogQueue.class);
			morphia.map(Context.class);
			morphia.map(ContextIndex.class);
			morphia.map(ContextQueue.class);
			morphia.map(Dictionary.class);
			morphia.map(DictionaryQueue.class);
			morphia.map(Template.class);
			morphia.map(TemplateDictionaryDAO.class);
			morphia.map(App.class);
			morphia.map(MasterIndex.class);
			
			ds = morphia.createDatastore(mongo, database); 
			ds.ensureIndexes();
			ds.ensureCaps();
		} catch (ArchiveeException e) {
			ArchiveeException.log(e, "Unable to load MongoDB properties succesfully", this);
		} catch (Exception e) {
			ArchiveeException.log(e, "Unable to connect to MongoDB database", this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @param <I>
	 * 
	 * @see biz.bidi.archivee.commons.dao.IArchiveeGenericDAO#save(java.lang.Object)
	 */
	@Override
	public Key<E> save(E entity) throws ArchiveeException {
		try {
			return ds.save(entity);
		} catch (Exception e) {
			throw new ArchiveeException(e, "Error while saving in mongoDB", entity, this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.dao.IArchiveeGenericDAO#delete(java.lang.Object, Q[])
	 */
	@Override
	public void delete(E entity, Query<E> query) throws ArchiveeException {
		try {
			if(entity != null) {
				ds.delete(entity);
			}
			if(query != null) {
				ds.delete(query);
			}
		} catch (Exception e) {
			throw new ArchiveeException(e, "Error while deleting in mongoDB", entity, query, this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.dao.IArchiveeGenericDAO#get(java.lang.Object)
	 */
	@Override
	public E get(E entity) throws ArchiveeException {
		E newEntity = null;
		try {
			newEntity = ds.get(entity);
		} catch (Exception e) {
			throw new ArchiveeException(e, "Error while getting entity in mongoDB", entity, newEntity, this);
		}
		return newEntity;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.dao.IArchiveeGenericDAO#find(java.lang.Object)
	 */
	@Override
	public Query<E> find(E entity) throws ArchiveeException {
		Query<E> query = null;
		try {
			query = ds.find((Class<E>)entity.getClass());
		} catch (Exception e) {
			throw new ArchiveeException(e, "Error while finding all entities in mongoDB", entity, query, this);
		}
		return query;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.dao.IArchiveeGenericDAO#getSize(java.lang.Object)
	 */
	@Override
	public long getSize(E entity) throws ArchiveeException {
		long size = 0;
		try {
			size = ds.getCount(entity);
		} catch (Exception e) {
			throw new ArchiveeException(e, "Error while getting entity size in mongoDB", entity, size, this);
		}
		return size;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.dao.IArchiveeGenericDAO#getQuerySize(java.lang.Object)
	 */
	@Override
	public long getQuerySize(Query<E> query) throws ArchiveeException {
		long size = 0;
		try {
			size = ds.getCount(query);
		} catch (Exception e) {
			throw new ArchiveeException(e, "Error while getting query size in mongoDB", query, size, this);
		}
		return size;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader#loadProperties(java.lang.String)
	 */
	@Override
	public void loadProperties(String prefixKey) throws ArchiveeException {
		ArchiveeProperties.loadProperties(this, prefixKey);
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @param database the database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

}
