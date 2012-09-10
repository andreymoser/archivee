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
package biz.bidi.archivee.components.simulations.log4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import biz.bidi.archivee.commons.ArchiveeConstants;
import biz.bidi.archivee.commons.utils.ArchiveeFileUtils;

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
	 * \\TODO 
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
		ArrayList<File> files = ArchiveeFileUtils.findFiles(logRegex, sourceLogsDirectory);
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
	 * Generate the files in the
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			File dir = new File(logFile.getParent());
			if (dir.isDirectory()) {
				/**
				 * Find the rotated log files and sort in descending order (*.log.99 to *.log)
				 */
				System.out.println("Thread dir " + dir.getName());
				File[] files = dir.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.contains(logFile.getName());
					}
				});
				
				ArrayList<File> filesArrayList = new ArrayList<File>(); 
				Collections.addAll(filesArrayList, files);
				Collections.sort(filesArrayList, new Comparator<File>() {
					@Override
					public int compare(File o1, File o2) {
						int cmp = 0;
						try {
							cmp = (getLogNumber(o1.getName()) < getLogNumber(o2.getName()))?1:-1;
						} catch (Exception e) {
							System.out.println("File 1: " + o1.getAbsolutePath() + " File 2: " + o2.getAbsolutePath());
							e.printStackTrace();
						}
						return cmp;
					}
					private int getLogNumber(String name) {
						String fileStr = name.replaceAll(logFile.getName(), "");
						if(fileStr.startsWith(".")) {
							fileStr = fileStr.replaceAll("\\.", "");
						}
						return (fileStr.length()==0)?0:Integer.parseInt(fileStr);
					}
				});
				
				/**
				 * Generate the log files in the output folder
				 */
			    File destDir = new File(destLogsDirectory);
			    		
				if(destDir.isFile()) {
					throw new Exception("Invalid destination log folder! It's a file: " + destLogsDirectory);
				}
				
				if(!destDir.exists()) {
					destDir.mkdirs();
				}
				
				String relativePath = dir.getAbsolutePath();
				relativePath = relativePath.length()>sourceLogsDirectory.length()?"/"+relativePath.replaceAll(sourceLogsDirectory, ""):"";
				
				File threadDir = new File(destDir.getAbsolutePath() + relativePath);
				if(!threadDir.exists()) {
					threadDir.mkdirs();
				}
				
				for (File file : filesArrayList) {
					System.out.println("Thread: writing from file " + file.getAbsolutePath());
					
					FileReader fileReader = new FileReader(file);
					int read = 0;
					File destFile = new File(threadDir.getAbsolutePath() + "/" + logFile.getName());
					
					if(destFile.exists()) {
						destFile.delete();
					}
					
					FileWriter fileWriter = new FileWriter(destFile);
					char[] buffer = new char[ArchiveeConstants.DEFAULT_BUFFER_SIZE];
					while((read = fileReader.read(buffer)) > 0) {
						Thread.sleep(200);
						fileWriter.write(buffer, 0, read);
						fileWriter.flush();
					}
					fileReader.close();
					fileWriter.close();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
