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

import java.nio.ByteBuffer;
import biz.bidi.archivee.commons.ArchiveeConstants;

/**
 * Byte utilities
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 5, 2012
 */
public class ArchiveeByteBufferUtils {
	
	
	private ByteBuffer byteBuffer;
	private long index;

	/**
	 * @param byteBuffer
	 */
	public ArchiveeByteBufferUtils(ByteBuffer byteBuffer) {
		super();
		this.byteBuffer = byteBuffer;
		this.index = 0;
	}

	/**
	 * @return the byteBuffer
	 */
	public ByteBuffer getByteBuffer() {
		return byteBuffer;
	}

	/**
	 * @param byteBuffer the byteBuffer to set
	 */
	public void setByteBuffer(ByteBuffer byteBuffer) {
		this.byteBuffer = byteBuffer;
	}
	
	private Character getNextChar() {
		Character character = byteBuffer.getChar();
		index = index + 2; //char two bytes read
		return character;
	}
	
	public String getNextLine() {
		return getNextLine(ArchiveeConstants.LINE_SEPARATOR);
	}

	
	public String getNextLine(String lineSeparator) {
		String line = "";
		
		int indexNewLine = 0;
		
		while(byteBuffer.hasRemaining()) {
			Character character = getNextChar();
			line += character;
			
			if(character == lineSeparator.charAt(indexNewLine)) {
				indexNewLine++;
				while(indexNewLine < lineSeparator.length()) {
					character = getNextChar();
					line += character;
					
					indexNewLine++;
					if(character != lineSeparator.charAt(indexNewLine)) {
						break;
					}
				}
				indexNewLine = 0;
			}
		}
		
		return line;
	}

}
