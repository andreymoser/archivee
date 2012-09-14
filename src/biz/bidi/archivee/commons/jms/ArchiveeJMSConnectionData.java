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
package biz.bidi.archivee.commons.jms;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.properties.ArchiveeProperties;
import biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 9, 2012
 */
public class ArchiveeJMSConnectionData implements IArchiveePropertiesLoader {
	
	/**
	 * The jms connection name - JNDI
	 */
	private String connectionJNDI;
	/**
	 * The jms connection username
	 */
	private String username;
	/**
	 * The jms connection password
	 */
	private String password;
	/**
	 * The jms connection factory classname
	 */
	private String connectionFactoryClassName;
	/**
	 * The jms connection host
	 */
	private String host;
	/**
	 * The jms connection port
	 */
	private int port;
	
	/**
	 * The jms connection factory JNDI 
	 */
	private String connectionFactoryJNDI;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader#loadProperties(String prefixKey)
	 */
	@Override
	public void loadProperties(String prefixKey) throws ArchiveeException {
		ArchiveeProperties.loadProperties(this, prefixKey);
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the connectionFactoryClassName
	 */
	public String getConnectionFactoryClassName() {
		return connectionFactoryClassName;
	}
	/**
	 * @param connectionFactoryClassName the connectionFactoryClassName to set
	 */
	public void setConnectionFactoryClassName(String connectionFactoryClassName) {
		this.connectionFactoryClassName = connectionFactoryClassName;
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
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the connectionFactoryJNDI
	 */
	public String getConnectionFactoryJNDI() {
		return connectionFactoryJNDI;
	}
	/**
	 * @param connectionFactoryJNDI the connectionFactoryJNDI to set
	 */
	public void setConnectionFactoryJNDI(String connectionFactoryJNDI) {
		this.connectionFactoryJNDI = connectionFactoryJNDI;
	}
	/**
	 * @return the jndi
	 */
	public String getConnectionJNDI() {
		return connectionJNDI;
	}
	/**
	 * @param connectionJNDI the jndi to set
	 */
	public void setConnectionJNDI(String connectionJNDI) {
		this.connectionJNDI = connectionJNDI;
	}

}
