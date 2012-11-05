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
package biz.bidi.archivee.commons.model.huffman;

import org.bson.types.ObjectId;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Oct 2, 2012
 */
public class HuffmanObjectIdNode implements Comparable<HuffmanObjectIdNode> {
	
	private HuffmanObjectIdNode parent;
	
	private HuffmanObjectIdNode left;
	
	private HuffmanObjectIdNode right;
	
	private int weight;
	
	private ObjectId value;
	
	private long code;
	
	private int level;

	/**
	 * @return the parent
	 */
	public HuffmanObjectIdNode getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(HuffmanObjectIdNode parent) {
		this.parent = parent;
	}

	/**
	 * @return the left
	 */
	public HuffmanObjectIdNode getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(HuffmanObjectIdNode left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public HuffmanObjectIdNode getRight() {
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(HuffmanObjectIdNode right) {
		this.right = right;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * @return the value
	 */
	public ObjectId getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(ObjectId value) {
		this.value = value;
	}

	/**
	 * @return the code
	 */
	public long getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(long code) {
		this.code = code;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return value.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(HuffmanObjectIdNode o) {
		
		if(this.getWeight() != o.getWeight()) {
			return this.getWeight() > o.getWeight() ? -1 : 1;
		}
		
		if(this.getValue() != null || o.getValue() != null) {
			if(this.getValue() != null && o.getValue() == null) {
				return 1;
			}
			
			if(this.getValue() == null && o.getValue() != null) {
				return -1;
			}
			
			if(this.getValue() != null && o.getValue() != null) {
				return this.getValue().compareTo(o.getValue());
			}
		}
		
		return this.hashCode() > o.hashCode() ? -1 : 1;
	}
	
}