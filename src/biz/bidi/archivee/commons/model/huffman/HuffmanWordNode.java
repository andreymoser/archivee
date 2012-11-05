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

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Oct 2, 2012
 */
public class HuffmanWordNode implements Comparable<HuffmanWordNode> {
	
	private HuffmanWordNode parent;
	
	private HuffmanWordNode left;
	
	private HuffmanWordNode right;
	
	private int weight;
	
	private String value;
	
	private long code;
	
	private int level;

	/**
	 * @return the parent
	 */
	public HuffmanWordNode getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(HuffmanWordNode parent) {
		this.parent = parent;
	}

	/**
	 * @return the left
	 */
	public HuffmanWordNode getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(HuffmanWordNode left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public HuffmanWordNode getRight() {
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(HuffmanWordNode right) {
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
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
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
		return value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(HuffmanWordNode o) {
		
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