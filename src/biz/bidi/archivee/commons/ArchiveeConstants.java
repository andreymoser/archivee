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
	/**
	 * The context data max size 
	 */
	public static final int CONTEXT_MAX_DATA_SIZE = MAX_BYTE_ARRAY_SIZE/4;
	/**
	 * The context index array list max size 
	 */
	public static final int CONTEXT_INDEX_MAX_DATA_SIZE = 100;
	/**
	 * The context queue data max size 
	 */
//	public static final int CONTEXT_QUEUE_MAX_DATA_SIZE = MAX_BYTE_ARRAY_SIZE;
	public static final int CONTEXT_QUEUE_MAX_DATA_SIZE = 3000;
	/**
	 * The context queue status delay 
	 */
	public static final int CONTEXT_QUEUE_STATUS_DELAY = 1000;
	
	/**
	 * The dot replacement value (☯ - Yin Yang) - used for mongodb fields which aren't allowed dot values - specifically in Map keys
	 */
	public static final String DOT_REPLACE_VALUE = "\u262F";	
	
	/**********************************************************************************
	 * DAO queries
	 */
	
	/**
	 * The context index key query 
	 */
	public static final String CONTEXT_INDEX_KEY_QUERY = "context.index.latest.query";
	/**
	 * The context index word query 
	 */
	public static final String CONTEXT_INDEX_WORD_QUERY = "context.index.word.query";
	/**
	 * The common log queue entries for a given pattern + app  
	 */
	public static final String LOG_QUEUE_PATTERN_QUERY = "log.queue.pattern.query";
	/**
	 * The log queue entries for a given app 
	 */
	public static final String LOG_QUEUE_APP_QUERY = "log.queue.app.query";
	/**
	 * The Master Index word query
	 */
	public static final String MASTER_INDEX_KEY_QUERY = "master.index.word.query";
	/**
	 * The Dictionary Queue Key Query
	 */
	public static final String DICTIONARY_QUEUE_KEY_QUERY = "dictionary.queue.key.query";
	/**
	 * The Dictionary Queue Key Template Query
	 */
	public static final String DICTIONARY_QUEUE_TEMPLATE_QUERY = "dictionary.queue.key.template.query";
	/**
	 * The Context Queue Key Query
	 */
	public static final String CONTEXT_QUEUE_KEY_QUERY = "context.queue.key.query";
	/**
	 * The Context Queue Next Key Query
	 */
	public static final String CONTEXT_QUEUE_NEXT_KEY_QUERY = "context.queue.next.key.query";
	/**
	 * The Template Queue Key Query
	 */
	public static final String TEMPLATE_KEY_QUERY = "template.key.query";
	/**
	 * The Template Pattern Key Query
	 */
	public static final String TEMPLATE_KEY_PATTERN_QUERY = "template.key.pattern.query";
	/**
	 * The Dictionary Key Query
	 */
	public static final String DICTIONARY_KEY_QUERY = "dictionary.key.query";
	/**
	 * The application name query
	 */
	public static final String APP_NAME_QUERY = "app.name.query";
	/**
	 * The pattern app query
	 */
	public static final String PATTERN_APP_QUERY = "pattern.app.query";
	/**
	 * The locker key query
	 */
	public static final String LOCKER_KEY_QUERY = "locker.key.query";
}
