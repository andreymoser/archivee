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
package biz.bidi.archivee.simulation;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * External App using Log4j simulation, it reads for an file expression (*.log) 
 * recursively and will generate logs based on its timestamp (log4j) and time range given by user.
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 2, 2012
 */
public class Log4jAppSimulation implements Runnable {

	/**
	 * The source directory
	 */
	private String sourceLogsDirectory;
	/**
	 * The output directory
	 */
	private String destLogsDirectory;
	/**
	 * The seconds range used in order to delimit the logs
	 */
	private int secondsRange;
	/**
	 * The log regex file that should be 
	 */
	private String logRegex;
	/**
	 * The thread file to process
	 */
	private File logFile;
	
	/**
	 * @param sourceLogsDirectory
	 * @param destLogsDirectory
	 * @param secondsRange
	 * @param logRegex
	 */
	public Log4jAppSimulation(String sourceLogsDirectory,
			String destLogsDirectory, int secondsRange, String logRegex) {
		super();
		this.sourceLogsDirectory = sourceLogsDirectory;
		this.destLogsDirectory = destLogsDirectory;
		this.secondsRange = secondsRange;
		this.logRegex = logRegex;
	}
	
	public Log4jAppSimulation(Log4jAppSimulation log4jAppSimulation) {
		this.sourceLogsDirectory = log4jAppSimulation.getSourceLogsDirectory();
		this.destLogsDirectory = log4jAppSimulation.getDestLogsDirectory();
		this.secondsRange = log4jAppSimulation.getSecondsRange();
		this.logRegex = log4jAppSimulation.getLogRegex();
	}

	/**
	 * Starts the simulation
	 */
	public void start() throws Exception {
		ArrayList<File> files = findFiles(null);
		if(files == null || files.size() == 0) {
			throw new Exception("Error: no files found for " + logRegex + " regex expression under " + sourceLogsDirectory + ". Please verify.");
		}
		
		
		Log4jAppSimulation[] log4jApp = new Log4jAppSimulation[files.size()];
		
		ExecutorService executor = Executors.newFixedThreadPool(files.size());
		
		for(int i = 0; i < log4jApp.length; i++) {
			log4jApp[i] = new Log4jAppSimulation(this);
			log4jApp[i].logFile = files.get(i);
			executor.execute(log4jApp[i]);
		}
		
		System.out.println("Info: Waiting for threads to terminate...");
		executor.shutdown();
		while(!executor.isTerminated()) {
			Thread.sleep(5000);
		}
		System.out.println("Info: threads terminated!");
	}

	/**
	 * @return
	 */
	private ArrayList<File> findFiles(ArrayList<File> files) throws Exception {
		
		if(files == null) {
			files = new ArrayList<File>();
			File file = new File(this.sourceLogsDirectory);
			files.add(file);
		}
		
		File lastFile = files.get(files.size() - 1);
		
		if(lastFile.isDirectory()) {
			files.remove(files.get(files.size() - 1));
			
			File[] filesDirectory = lastFile.listFiles();
			for(File file : filesDirectory) {
				files.add(file);
				
				files = findFiles(files);
			}
			return files;
		}

		Pattern logFilePattern = Pattern.compile(getLogRegex());
		
		Matcher matcher = logFilePattern.matcher(lastFile.getName());

		if(!matcher.matches()) {
			files.remove(files.get(files.size() - 1));
		} else {
			System.out.println("Info: found log " + lastFile.getAbsoluteFile());
		}
		
		return files;
	}

	/**
	 * Generate the files in the
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		//TODO continue here
		System.out.println("Hey " + this.logFile.getName());
		
	}
	
	/**
	 * @return the sourceLogsDirectory
	 */
	public String getSourceLogsDirectory() {
		return sourceLogsDirectory;
	}

	/**
	 * @param sourceLogsDirectory the sourceLogsDirectory to set
	 */
	public void setSourceLogsDirectory(String sourceLogsDirectory) {
		this.sourceLogsDirectory = sourceLogsDirectory;
	}

	/**
	 * @return the destLogsDirectory
	 */
	public String getDestLogsDirectory() {
		return destLogsDirectory;
	}

	/**
	 * @param destLogsDirectory the destLogsDirectory to set
	 */
	public void setDestLogsDirectory(String destLogsDirectory) {
		this.destLogsDirectory = destLogsDirectory;
	}

	/**
	 * @return the secondsRange
	 */
	public int getSecondsRange() {
		return secondsRange;
	}

	/**
	 * @param secondsRange the secondsRange to set
	 */
	public void setSecondsRange(int secondsRange) {
		this.secondsRange = secondsRange;
	}

	/**
	 * @return the logRegex
	 */
	public String getLogRegex() {
		return logRegex;
	}

	/**
	 * @param logRegex the logRegex to set
	 */
	public void setLogRegex(String logRegex) {
		this.logRegex = logRegex;
	}

}
