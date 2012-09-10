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
package biz.bidi.archivee.components.listeners.logsender;

import biz.bidi.archivee.commons.ArchiveeException;
import biz.bidi.archivee.commons.interfaces.IArchiveeFactory;
import biz.bidi.archivee.components.listeners.logsender.jms.JMSLogSender;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 7, 2012
 */
public class LogSenderFactory implements IArchiveeFactory<ILogSender,Object> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.IArchiveeFactory#createInstance(java.lang.Object)
	 */
	@Override
	public ILogSender createInstance(Object object) throws ArchiveeException {
		return new JMSLogSender();
	}

}
