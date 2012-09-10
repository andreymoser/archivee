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

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import biz.bidi.archivee.commons.ArchiveeConstants;

/**
 * File utilities
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 4, 2012
 */
public class ArchiveeFileUtils {

	/**
	 * Find files recursively filtering files thru the given regex
	 * @param filenameRegex
	 * @param directory
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<File> findFiles(String filenameRegex, String directory) throws Exception {
		return findFiles(null, filenameRegex, directory);
	}
	
	/**
	 * @return
	 */
	private static ArrayList<File> findFiles(ArrayList<File> files, String filenameRegex, String directory) throws Exception {
		
		if(files == null) {
			files = new ArrayList<File>();
			File file = new File(directory);
			files.add(file);
		}
		
		File lastFile = files.get(files.size() - 1);
		
		if(lastFile.isDirectory()) {
			files.remove(files.get(files.size() - 1));
			
			File[] filesDirectory = lastFile.listFiles();
			for(File file : filesDirectory) {
				files.add(file);
				
				files = findFiles(files, filenameRegex, directory);
			}
			return files;
		}

		Pattern logFilePattern = Pattern.compile(filenameRegex);		
		Matcher matcher = logFilePattern.matcher(lastFile.getName());

		if(!matcher.matches()) {
			files.remove(lastFile);
		} else {
			System.out.println("Info: found log " + lastFile.getAbsoluteFile());
		}
		
		return files;
	}
	
	/**
	 * Returns the filename for the given full file path 
	 * @param pathFile
	 * @return
	 */
	public static String getFileName(String pathFile) {
		return pathFile.substring(pathFile.lastIndexOf(ArchiveeConstants.FILE_SEPARATOR)+(ArchiveeConstants.FILE_SEPARATOR.length()), pathFile.length());
	}

}
