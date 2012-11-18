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
package biz.bidi.archivee.commons.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

import biz.bidi.archivee.commons.properties.ArchiveeProperties;
import biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader;
import biz.bidi.archivee.commons.utils.ArchiveeLogger;

import com.mongodb.MongoException;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 6, 2012
 */
@SuppressWarnings("serial")
public class ArchiveeException extends Exception implements IArchiveeException, IArchiveePropertiesLoader {

	protected String message;
	protected Object instanceError;
	protected Object[] objects;
	protected Exception e;
	protected ArchiveeError error;
	
	protected final static ArchiveeException instance = new ArchiveeException();
	protected boolean printStackTrace;
	private ArchiveeException() {
		try {
			loadProperties(null);
		} catch (ArchiveeException e) {
			e.printStackTrace();
		}
	}
	
	public ArchiveeException(Exception e, Object... objects) {
		this.printStackTrace = instance.isPrintStackTrace();
		this.e = e;
		if(objects.length > 0 && objects[0] != null) {
			instanceError = objects[0];
		}
		this.objects = objects;
	}
	
	public ArchiveeException(String message, Object... objects) {
		this.printStackTrace = instance.isPrintStackTrace();
		this.message = message;
		if(objects.length > 0 && objects[0] != null) {
			instanceError = objects[0];
		}
		this.objects = objects;
	}
	
	public ArchiveeException(Exception e, String message, Object... objects) {
		this.printStackTrace = instance.isPrintStackTrace();
		this.e = e;
		this.message = message;
		this.objects = objects;
		if(objects.length > 0 && objects[0] != null) {
			instanceError = objects[0];
		}
	}

	public static void logError(String message, Object... objects) {
		ArchiveeException exception = new ArchiveeException(null,message,objects);
		exception.error(null,null);
	}
	
	public static void error(Exception e, String message, Object... objects) {
		ArchiveeException exception = null;
		if(e instanceof ArchiveeException) {
			exception = (ArchiveeException) e;
			exception.error(null,null);
			exception = new ArchiveeException(message,objects);
		} else {
			exception  = new ArchiveeException(e,message,objects);
		}
		exception.error(null,null);
	}
	
	private String error(Object... objects) {
		if(this.objects == null || this.objects.length == 0) {
			this.objects = objects;
			
			if(this.objects == null || this.objects.length == 0) {
				return "";
			}
		}
		
		return ArchiveeLogger.getInstance().getInstancesAttributesValue(this.objects);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.exceptions.IArchiveeException#error(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void error(String message, Object... objects) {
		if(this.message == null || this.message == "") {
			this.message = message;
		}
		if(this.objects == null || this.objects.length == 0) {
			this.objects = objects;
		}
		if(instanceError == null) {
			if(this.objects == null && this.objects.length > 0) {
				instanceError = this.objects[0];
			}
		}
		if(instanceError == null) {
			instanceError = this;
		}
		
		String objectsString = error(objects);
		
		if(e != null) {
			if(e instanceof ArchiveeException) {
				ArchiveeException ae = (ArchiveeException) e; 
				ae.error(null, null);
			} else {
				ArchiveeLogger.getInstance().error(e, e.getMessage() + (printStackTrace?" - " + getStackTrace(e):""));
			}
		}
		ArchiveeLogger.getInstance().error(instanceError, this.message + " " + objectsString + " - " + getStackTrace(this));
	}
	
	public static String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
	public boolean isMongodbDuplicateKey() {
		if(e == null) {
			return false;
		}
		
		if(e instanceof MongoException) {
			MongoException me = (MongoException) e; 
			if(me.getCode() == 11000) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * @return the error
	 */
	public ArchiveeError getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(ArchiveeError error) {
		this.error = error;
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
	 * @return the printStackTrace
	 */
	public boolean isPrintStackTrace() {
		return printStackTrace;
	}

	/**
	 * @param printStackTrace the printStackTrace to set
	 */
	public void setPrintStackTrace(boolean printStackTrace) {
		this.printStackTrace = printStackTrace;
	}
	
}
