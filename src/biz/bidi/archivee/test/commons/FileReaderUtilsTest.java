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
package biz.bidi.archivee.test.commons;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 12, 2012
 */
public class FileReaderUtilsTest {

	/**
	 * The file
	 */
	private File file;
	/**
	 * The scanner file
	 */
	private Scanner scanner;

	/**
	 * @param file
	 */
	public FileReaderUtilsTest(File file) {
		super();
		this.file = file;
		try {
			this.scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Has next
	 */
	public boolean hasNext() {
		boolean hasNext = scanner.hasNext(); 
		if(!hasNext) {
			scanner.close();
		}
		return hasNext;
	}

	/**
	 * Reads the next line
	 * @return
	 */
	public String readLine() {
		String line = "";
		try {
			line = scanner.nextLine();
		}
		catch (NoSuchElementException e) {
			line = "";
		}
		return line;
	}
	
	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

}
