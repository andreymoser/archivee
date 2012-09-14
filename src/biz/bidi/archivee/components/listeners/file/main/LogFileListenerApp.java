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
package biz.bidi.archivee.components.listeners.file.main;

import java.util.Scanner;

import biz.bidi.archivee.commons.properties.ArchiveeProperties;
import biz.bidi.archivee.components.listeners.file.FileListener;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 10, 2012
 */
public class LogFileListenerApp {

	/**
	 * Usage: java LogFileListenerApp logsDirectory regexLogFilename |-p file|<blank>
	 * -p file - reads properties file by the given properties file (FileListener. prefix), if file is empty will use default path {@link ArchiveeProperties#path} 
	 * <blank> - no arguments - console input
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			FileListener fileListener = new FileListener();

			if(args.length == 2 && !args[0].equals("-p")) {
				fileListener.setLogsDirectory(args[0]);
				fileListener.setRegexLogFilename(args[1]);
			} else if (args.length >= 1 && args[0].equals("-p")) {
				if(args.length > 1) { ArchiveeProperties.load(args[1]); }
				fileListener.loadProperties(null);
			} else {
				Scanner scanner = new Scanner(System.in);
				System.out.print("Source logs directory: ");
				fileListener.setLogsDirectory(scanner.nextLine());
				System.out.print("Regex log filename: ");
				fileListener.setRegexLogFilename(scanner.nextLine());
			}
			
			System.out.println("Source logs directory: " + fileListener.getLogsDirectory());
			System.out.println("Regex log filename: " + fileListener.getRegexLogFilename());
						
			fileListener.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
