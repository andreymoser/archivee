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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 6, 2012
 */
@SuppressWarnings("serial")
public class ArchiveeException extends Exception implements IArchiveeException {

	protected String message;
	protected Object[] objects;
	protected Exception e;
	
	public ArchiveeException(Exception e) {
		this.e = e;
	}
	
	public ArchiveeException(Exception e, Object... objects) {
		this.e = e;
		this.objects = objects;
	}
	
	public ArchiveeException(String message, Object... objects) {
		this.message = message;
		this.objects = objects;
	}
	
	public ArchiveeException(Exception e, String message, Object... objects) {
		this.e = e;
		this.message = message;
		this.objects = objects;
	}
	
	public static void log(Object instanceLog, String message, Object... objects) {
		ArchiveeException exception = new ArchiveeException(null,message,objects);
		exception.error(null,null);
	}
	
	public static void log(Exception e, String message, Object... objects) {
		ArchiveeException exception = null;
		if(e instanceof ArchiveeException) {
			exception  = (ArchiveeException) e;
		} else {
			exception  = new ArchiveeException(e,message,objects);
		}
		exception.error(null,null);
		
	}
	
	private void error(Object... objects) {
			
			if(this.objects == null || this.objects.length == 0) {
				this.objects = objects;
				
				if(this.objects == null || this.objects.length == 0) {
					return;
				}
			}
			
			String objectMessage = "";
			for(Object object : this.objects) {
				if(object == null) {
					continue;
				}
				
				objectMessage+="{ObjectName="+object.getClass().getName() + ";";
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
				objectMessage+="}\t";
			}
			System.out.println(objectMessage);
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
		System.out.println(this.getClass().getName()+":"+this.message);
		if(e !=  null) {
			System.out.println(e.getClass().getName()+":"+e.getMessage());
		}
		
		error(objects);
		
		if(e !=  null) {
			e.printStackTrace();
		} else {
			this.printStackTrace();
		}
	}
	
}
