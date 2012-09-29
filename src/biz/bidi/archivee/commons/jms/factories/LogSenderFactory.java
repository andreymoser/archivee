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
package biz.bidi.archivee.commons.jms.factories;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.factories.ArchiveeSingletonFactory;
import biz.bidi.archivee.commons.interfaces.ILogSender;
import biz.bidi.archivee.commons.jms.senders.JMSLogParserSender;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 7, 2012
 */
public class LogSenderFactory extends ArchiveeSingletonFactory<ILogSender,Object> {

	static {
		instance = new JMSLogParserSender();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.factories.IArchiveeFactory#createInstance(java.lang.Object)
	 */
	@SuppressWarnings("static-access")
	@Override
	public ILogSender createInstance(Object object) throws ArchiveeException {
		return (ILogSender) this.getInstance();
	}

}
