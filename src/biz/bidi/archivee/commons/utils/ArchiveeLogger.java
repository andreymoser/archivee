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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.bidi.archivee.commons.interfaces.ILogger;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Oct 18, 2012
 */
public class ArchiveeLogger implements ILogger {

	public static ArchiveeLogger instance = new ArchiveeLogger();
	
	private Logger logger;
	
	private void setupLogger(Object instance) {
		logger = LoggerFactory.getLogger(instance.getClass());
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
	 * @see biz.bidi.archivee.commons.interfaces.ILogger#error(java.lang.Object, java.lang.String)
	 */
	@Override
	public void error(Object instance, String message) {
		setupLogger(instance);
		logger.error(message);
	}
	
}
