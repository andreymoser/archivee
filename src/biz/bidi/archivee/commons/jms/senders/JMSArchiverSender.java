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
package biz.bidi.archivee.commons.jms.senders;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.interfaces.IArchiverSender;
import biz.bidi.archivee.commons.jms.ArchiveeJMSQueue;
import biz.bidi.archivee.commons.model.xml.PatternMessage;
import biz.bidi.archivee.commons.properties.ArchiveeProperties;
import biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader;
import biz.bidi.archivee.commons.xml.ArchiveeXmlParser;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Oct 28, 2012
 */
public class JMSArchiverSender extends ArchiveeJMSQueue implements IArchiverSender, IArchiveePropertiesLoader {

	/**
	 * The connection name
	 */
	private String connectionName;
	
	public JMSArchiverSender() {
		super();
		loadProperties(this.getClass().getSimpleName() + ".");

		try {
			this.open();
		} catch (ArchiveeException e) {
			ArchiveeException.error(e, "Error while connecting to queue", this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.IPatternSender#sendPatternMessage(biz.bidi.archivee.commons.model.xml.PatternMessage)
	 */
	@Override
	public void sendPatternMessage(PatternMessage message) throws ArchiveeException {
		send(ArchiveeXmlParser.convertoToXml(message));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader#loadProperties(java.lang.String)
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
