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
package biz.bidi.archivee.commons.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import biz.bidi.archivee.commons.ArchiveeConstants;
import biz.bidi.archivee.commons.exceptions.ArchiveeException;

/**
 * Common properties class between archivee components
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 2, 2012
 */
public class ArchiveeProperties {

	private static Properties properties = new Properties();
	/**
	 * Default properties path {@link ArchiveeConstants#PROPERTIES_PATH}
	 */
	private static final String path = ArchiveeConstants.PROPERTIES_PATH;
	
	static {
		load(path);
	}
	
	public static String get(String key) {
		return properties.getProperty(key);
	}
	
	public static void load(String path) {
		try {
			properties.load(new FileInputStream(new File(path)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Automatically loads all the properties invoking Java beans methods
	 * @param object
	 * @param prefixKey - if null it will use default value:"SimpleClassName."
	 * @throws ArchiveeException
	 */
	public static void loadProperties(Object object) throws ArchiveeException {
		loadProperties(object, null);
	}
	
	/**
	 * Automatically loads all the properties invoking Java beans methods
	 * @param object
	 * @param prefixKey - if null it will use default value:"SimpleClassName."
	 * @throws ArchiveeException
	 */
	public static void loadProperties(Object object, String prefixKey) throws ArchiveeException {
		if(prefixKey == null) {
			prefixKey = object.getClass().getSimpleName() + ".";
		}
		
		Method[] methods = object.getClass().getMethods();
		for(Method method : methods) {
			if(!method.getName().startsWith("set") || method.getParameterTypes().length != 1)
			{
				continue;
			}
			String type = method.getParameterTypes()[0].getSimpleName();
			String name = method.getName().substring(3);
			
			if(name.length() <= 0) {
				continue;
			}
			String headStr = name.substring(0,1).toLowerCase();
			
			if(name.length() == 1) {
				name = headStr;
			} else {
				name = headStr + name.substring(1);
				
			}
			
			Object param = null; 
			
			if(type.equals("String")) {
				param = ArchiveeProperties.get(prefixKey + name);
			}
			if(type.equals("int") || type.equals("Integer")) {
				param = Integer.parseInt(ArchiveeProperties.get(prefixKey + name));
			}
			if(type.equals("double") || type.equals("Double")) {
				param = Double.parseDouble(ArchiveeProperties.get(prefixKey + name));
			}
			if(type.equals("long") || type.equals("Long")) {
				param = Long.parseLong(ArchiveeProperties.get(prefixKey + name));
			}
			if(type.equals("float") || type.equals("Float")) {
				param = Float.parseFloat(ArchiveeProperties.get(prefixKey + name));
			}
			if(type.equals("short") || type.equals("Short")) {
				param = Short.parseShort(ArchiveeProperties.get(prefixKey + name));
			}
			
			if(param != null) {
				try {
					method.invoke(object, param);
				} catch (IllegalArgumentException e) {
					throw new ArchiveeException(e,"Error while trying to load properties for " + object.getClass().getName() + "IllegalArgumentException",object,prefixKey);
				} catch (IllegalAccessException e) {
					throw new ArchiveeException(e,"Error while trying to load properties for " + object.getClass().getName() + "IllegalAccessException",object,prefixKey);
				} catch (InvocationTargetException e) {
					throw new ArchiveeException(e,"Error while trying to load properties for " + object.getClass().getName() + "InvocationTargetException",object,prefixKey);
				}
			}
		}
	}

}
