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
package biz.bidi.archivee.components.listeners.logsender.jms;

import biz.bidi.archivee.commons.ArchiveeException;
import biz.bidi.archivee.commons.ArchiveeProperties;
import biz.bidi.archivee.commons.dao.jms.ArchiveeJMSConnectionData;
import biz.bidi.archivee.commons.dao.jms.ArchiveeJMSTopicDAO;
import biz.bidi.archivee.commons.dao.properties.IArchiveePropertiesLoader;
import biz.bidi.archivee.components.listeners.logsender.ILogSender;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 7, 2012
 */
public class JMSLogSender extends ArchiveeJMSTopicDAO implements ILogSender, IArchiveePropertiesLoader {

	/**
	 * The connection name
	 */
	private String connectionName;
	
	public JMSLogSender() {
		connectionData = new ArchiveeJMSConnectionData();
		loadProperties(this.getClass().getSimpleName() + ".");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.components.listeners.logsender.ILogSender#sendLogLine(java.lang.String)
	 */
	@Override
	public void sendLogLine(String line) throws ArchiveeException {
		send(line);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.dao.properties.IArchiveePropertiesLoader#loadProperties(java.lang.String)
	 */
	@Override
	public void loadProperties(String prefixKey) {
		try {
			ArchiveeProperties.loadProperties(this, prefixKey );
			
			connectionData.loadProperties(prefixKey + this.getConnectionName() + ".");
		} catch (ArchiveeException e) {
			e.error("Unable to load properties",prefixKey);
		}
	}

	/**
	 * @return the name
	 */
	public String getConnectionName() {
		return connectionName;
	}

	/**
	 * @param connectionName the name to set
	 */
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

}
