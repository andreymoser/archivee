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
package biz.bidi.archivee.components.simulations.log4j.main;

import java.util.Scanner;

import biz.bidi.archivee.commons.ArchiveeProperties;
import biz.bidi.archivee.components.simulations.log4j.Log4jAppSimulation;

/**
 * Main class for {@link Log4jAppSimulation}
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 2, 2012
 */
public class Log4jAppMainSimulation {

	/**
	 * Usage: java Log4jAppMainSimulation srcFileLog dstFileLog seconds regex|-p file|<blank>
	 * -p file - reads properties file by the given properties file, if file is empty will use default path {@link ArchiveeProperties#path} 
	 * <blank> - no arguments - console input
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String srcFileLog = "";
			String dstFileLog = "";
			Integer seconds = 0;
			String regex = "";

			if(args.length == 4) {
				srcFileLog = args[0];
				dstFileLog = args[1];
				seconds = Integer.parseInt(args[2]);
				regex = args[3];
			} else if (args.length >= 1 && args[0].equals("-p")) {
				if(args.length > 1) { ArchiveeProperties.load(args[1]); }
				
				srcFileLog = ArchiveeProperties.get("Log4jAppSimulation.sourceLogsDirectory");
				dstFileLog = ArchiveeProperties.get("Log4jAppSimulation.destLogsDirectory");
				seconds = Integer.parseInt(ArchiveeProperties.get("Log4jAppSimulation.secondsRange"));
				regex = ArchiveeProperties.get("Log4jAppSimulation.logRegex");
			} else {
				Scanner scanner = new Scanner(System.in);
				System.out.print("Source logs directory: ");
				srcFileLog = scanner.nextLine();
				System.out.print("Dest logs directory: ");
				dstFileLog = scanner.nextLine();
				System.out.print("Seconds range: ");
				seconds = scanner.nextInt();
				System.out.print("Log filename regex: ");
				regex = scanner.nextLine();
			}
			
			System.out.println("Source logs directory: " + srcFileLog);
			System.out.println("Dest logs directory: " + dstFileLog);
			System.out.println("Seconds range: " + seconds);
			System.out.println("Log filename regex: " + regex);
			
			Log4jAppSimulation log4jApp = new Log4jAppSimulation(srcFileLog, dstFileLog, seconds, regex);
			log4jApp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
