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
package biz.bidi.archivee.commons;

/**
 * Commons contants between Archivee components
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 2, 2012
 */
public class ArchiveeConstants {

	/**
	 * Default properties path
	 */
	public static final String PROPERTIES_PATH = "./resources/archivee.properties";
	/**
	 * File separator
	 */
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	/**
	 * OS Windows flag
	 */
	public static boolean IS_WINDOWS_OS = System.getProperty("os.name").startsWith("Windows");
	/**
	 * Line separator
	 */
	public static String LINE_SEPARATOR = System.getProperty("line.separator");
	/**
	 * Default buffer size
	 */
	public static int DEFAULT_BUFFER_SIZE = 4096;
	/**
	 * The default thread sleep used in main threads loops 
	 */
	public static int DEFAULT_MAIN_THREAD_SLEEP_LOOP = 5000;
	/**
	 * The max byte array size 
	 */
	public static int MAX_BYTE_ARRAY_SIZE = 600000;
}
