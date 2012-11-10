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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.bson.types.ObjectId;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.interfaces.ILogger;
import biz.bidi.archivee.commons.properties.ArchiveeProperties;
import biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Oct 18, 2012
 */
public class ArchiveeLogger implements ILogger, IArchiveePropertiesLoader {

	private String log4jPropertiesFile;
	
	private static ArchiveeLogger instance = new ArchiveeLogger();
	
	private Logger logger;
	
	public ArchiveeLogger() {
		try {
			loadProperties(null);
			PropertyConfigurator.configure(log4jPropertiesFile);
		} catch (ArchiveeException e) {
			e.printStackTrace();
		}
	}
	
	private void setupLogger(Object instance) {
		logger = Logger.getLogger(instance.getClass());
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.ILogger#warn(java.lang.Object, java.lang.String)
	 */
	@Override
	public void warn(Object instance, String message) {
		setupLogger(instance);
		logger.warn(message);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.ILogger#warn(java.lang.Object, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void warn(Object instance, String message, Object... objects) {
		warn(instance, message + " " + getInstancesAttributesValue(objects));
	}
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.ILogger#debug(java.lang.Object, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void debug(Object instance, String message, Object... objects) {
		debug(instance, message + " " + getInstancesAttributesValue(objects));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.ILogger#debug(java.lang.Object, java.lang.String)
	 */
	@Override
	public void debug(Object instance, String message) {
		setupLogger(instance);
		logger.debug(message);
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.ILogger#info(java.lang.Object, java.lang.String)
	 */
	@Override
	public void info(Object instance, String message) {
		setupLogger(instance);
		logger.info(message);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.ILogger#info(java.lang.Object, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void info(Object instance, String message, Object... objects) {
		info(instance, message + " " + getInstancesAttributesValue(objects));
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.ILogger#error(java.lang.Object, java.lang.String)
	 */
	@Override
	public void error(Object instance, String message) {
		setupLogger(instance);
		logger.error(message);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader#loadProperties(java.lang.String)
	 */
	@Override
	public void loadProperties(String prefixKey) throws ArchiveeException {
		ArchiveeProperties.loadProperties(this);
	}

	/**
	 * @return the log4jPropertiesFile
	 */
	public String getLog4jPropertiesFile() {
		return log4jPropertiesFile;
	}

	/**
	 * @param log4jPropertiesFile the log4jPropertiesFile to set
	 */
	public void setLog4jPropertiesFile(String log4jPropertiesFile) {
		this.log4jPropertiesFile = log4jPropertiesFile;
	}
	
	/**
	 * Returns all the objects attributes (java bean standard) in a human readable mode
	 * @param objects
	 * @return
	 */
	public String getInstancesAttributesValue(Object... objects) {
		String objectMessage = "";
		for(Object object : objects) {
			if(object == null) {
				continue;
			}
			
			objectMessage+="{Name="+object.getClass().getSimpleName() + ";";
			
			if(object instanceof String || object instanceof Integer || object instanceof Double ||
				object instanceof Long  || object instanceof Float  || object instanceof Boolean) {
				objectMessage+="value=\"" + object.toString() + "\";";
			} else {
				for(Method method : object.getClass().getMethods()) {
					if(!(method.getName().startsWith("get") && method.getName().length() > 3 || 
					   method.getName().startsWith("is"))) {
						continue;
					}
					Class c = method.getReturnType();
					
					try {
						if(c.equals(String.class)) {
							objectMessage+=method.getName().substring(3) + "=\"" + method.invoke(object, null) + "\";";
						} else if(c.equals(Integer.class)) {
							objectMessage+=method.getName().substring(3) + "(int)=" + method.invoke(object, null) + ";";						
						} else if(c.equals(Double.class)) {
							objectMessage+=method.getName().substring(3) + "(double)=" + method.invoke(object, null) + ";";						
						} else if(c.equals(Long.class)) {
							objectMessage+=method.getName().substring(3) + "(long)=" + method.invoke(object, null) + ";";						
						} else if(c.equals(Float.class)) {
							objectMessage+=method.getName().substring(3) + "(float)=" + method.invoke(object, null) + ";";						
						} else if(c.equals(Boolean.class)) {
							objectMessage+=method.getName() + "(boolean)=" + method.invoke(object, null) + ";";						
						} else if(c.equals(ObjectId.class)) {
							ObjectId objectId = (ObjectId) method.invoke(object, null);
							if(objectId != null) {
								objectMessage+=method.getName().substring(3) + "(ObjectId)=" + objectId.toStringMongod() + ";";						
							} else {
								objectMessage+=method.getName().substring(3) + "(" + c.getSimpleName() + ")=" + method.invoke(object, null) + ";";						
							}
						} else if(c.equals(Class.class)) {
							//ignore						
						} else {
							objectMessage+=method.getName().substring(3) + "(" + c.getSimpleName() + ")=" + method.invoke(object, null) + ";";						
						}
					} catch (IllegalArgumentException e) {
						//ignore
					} catch (IllegalAccessException e) {
						//ignore
					} catch (InvocationTargetException e) {
						//ignore
					}
				}
			}
			
			objectMessage+="}\t";
		}
		
		return objectMessage;
	}

	/**
	 * @return the instance
	 */
	public static ArchiveeLogger getInstance() {
		return instance;
	}
}