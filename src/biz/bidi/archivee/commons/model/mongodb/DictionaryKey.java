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
package biz.bidi.archivee.commons.model.mongodb;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 27, 2012
 */
public class DictionaryKey {

	private long patternId;
	
	private int patternPath;
	
	private int elementIndex;

	/**
	 * @return the patternId
	 */
	public long getPatternId() {
		return patternId;
	}

	/**
	 * @param patternId the patternId to set
	 */
	public void setPatternId(long patternId) {
		this.patternId = patternId;
	}

	/**
	 * @return the patternPath
	 */
	public int getPatternPath() {
		return patternPath;
	}

	/**
	 * @param patternPath the patternPath to set
	 */
	public void setPatternPath(int patternPath) {
		this.patternPath = patternPath;
	}

	/**
	 * @return the elementIndex
	 */
	public int getElementIndex() {
		return elementIndex;
	}

	/**
	 * @param elementIndex the elementIndex to set
	 */
	public void setElementIndex(int elementIndex) {
		this.elementIndex = elementIndex;
	}
}
