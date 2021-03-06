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
package biz.bidi.archivee.commons.factories;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.interfaces.IArchiverSender;
import biz.bidi.archivee.commons.interfaces.ICompressorSender;
import biz.bidi.archivee.commons.interfaces.ILogSender;
import biz.bidi.archivee.commons.interfaces.IPatternSender;
import biz.bidi.archivee.commons.jms.IArchiveeMessaging;
import biz.bidi.archivee.commons.jms.factories.CompressorSenderFactory;
import biz.bidi.archivee.commons.jms.factories.LogSenderFactory;
import biz.bidi.archivee.commons.jms.factories.PatternSenderFactory;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 29, 2012
 */
@SuppressWarnings("rawtypes")
public class JMSFactoryManager implements IArchiveeFactoryManager {

	protected IArchiveeFactoryManager archiveeFactoryManager;
	
	protected IArchiveeFactory logSenderFactory;
	protected IArchiveeFactory patternSenderFactory;
	protected IArchiveeFactory compressorSenderFactory;
	protected IArchiveeFactory archiverSenderFactory;
	
	public JMSFactoryManager(IArchiveeFactoryManager archiveeFactoryManager) {
		this.archiveeFactoryManager = archiveeFactoryManager;
		
		logSenderFactory = new LogSenderFactory();
		patternSenderFactory = new PatternSenderFactory();
		compressorSenderFactory = new CompressorSenderFactory();
		archiverSenderFactory = new ArchiverSenderFactory();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.interfaces.IArchiveeSingleTonFactoryManager#getFactoryInstance(java.lang.Class)
	 */
	@Override
	public IArchiveeFactory getFactoryInstance(Class classObject) throws ArchiveeException {
		return getFactoryInstance(classObject, null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.factories.IArchiveeFactoryManager#getFactoryInstance(java.lang.Class, java.lang.Object)
	 */
	@Override
	public IArchiveeFactory getFactoryInstance(Class interfaceClass, Class classObject) throws ArchiveeException {
		IArchiveeFactory factory = null;
		
		if(interfaceClass == IArchiveeMessaging.class && classObject != null) {
			if(classObject == ILogSender.class) {
				factory = logSenderFactory;
			}
			if(classObject == IPatternSender.class) {
				factory = patternSenderFactory;
			}
			if(classObject == ICompressorSender.class) {
				factory = compressorSenderFactory;
			}
			if(classObject == IArchiverSender.class) {
				factory = archiverSenderFactory;
			}
		}
		
		return factory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.factories.IArchiveeFactoryManager#getManagerInstance()
	 */
	@Override
	public IArchiveeFactoryManager getManagerInstance() {
		return archiveeFactoryManager;
	}
}
