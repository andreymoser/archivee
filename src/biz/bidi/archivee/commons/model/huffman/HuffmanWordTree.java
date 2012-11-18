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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import biz.bidi.archivee.commons.utils.ArchiveeByteUtils;

/**
 * 
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Oct 2, 2012
 */
public class HuffmanWordTree {

	private HuffmanWordNode root;
	
	private TreeSet<HuffmanWordNode> treeSet;
	
	private TreeSet<HuffmanWordNode> nodes;
	
	public void buildTree(ArrayList<String> strings) {
		// find out weight for all symbols		
		HashMap<String, Integer> refDictionary = new HashMap<String, Integer>();
		
		for(String value : strings) {
			Integer weight = 0;
			if(refDictionary.containsKey(value)) {				
				weight = refDictionary.get(value);
			} 
			weight++;
			refDictionary.put(value, weight);
		}
		
		buildTree(refDictionary);
	}
	
	public void buildTree(HashMap<String, Integer> entries) {
		nodes = new TreeSet<HuffmanWordNode>();
		
		// builds priority queue
		treeSet = new TreeSet<HuffmanWordNode>();
		
		for(String value : entries.keySet()) {
			int weight = entries.get(value);
			
			HuffmanWordNode node = new HuffmanWordNode();
			node.setWeight(weight);
			node.setValue(value);
			
			treeSet.add(node);
		}
		
		System.out.println();
		
		// generates the huffman tree
		while(treeSet.size() > 1) {
			HuffmanWordNode right = treeSet.pollFirst();
			HuffmanWordNode left = treeSet.pollFirst();
			HuffmanWordNode parent = new HuffmanWordNode();
			parent.setWeight(left.getWeight() + right.getWeight());
			parent.setValue(null);
			
			//link nodes
			left.setParent(parent);
			right.setParent(parent);
			parent.setLeft(left);
			parent.setRight(right);
			
			treeSet.add(parent);
			
			if(left.getValue() != null) { nodes.add(left); }
			if(right.getValue() != null) { nodes.add(right); }
		}
		
		root = treeSet.pollFirst();
		
		if(entries.size() == 1) {
			root.setLevel(1);
			nodes.add(root);
		} else {
			generateCodes(root,0,0);
		}
		
	}

	public HuffmanWordNode getNode(String value) {
		HuffmanWordNode found = null;
		
		for(HuffmanWordNode node : nodes) {
			if(node.getValue().equals(value)) {
				found = node;
				break;
			}
		}
		
		return found;
	}
	
	public HuffmanWordNode getNode(long code) {
		HuffmanWordNode found = null;
		
		for(HuffmanWordNode node : nodes) {
			if(node.getCode() == code) {
				found = node;
				break;
			}
		}
		
		return found;
	}
	
	public HuffmanWordNode getUniqueNode(long code,int length) {
		HuffmanWordNode found = null;
		
		int count = 0;
		
		for(HuffmanWordNode node : nodes) {
			if(node.getCode() == code && node.getLevel() == length) {
				found = node;
				count++;
			}
		}

		if(count != 1) {
			found = null;
		}
		
		return found;
	}
	
	private void generateCodes(HuffmanWordNode node, int level, long code) {
		if(node == root) {
			if(node.getLeft()!=null) {
				generateCodes(node.getLeft(),level + 1,0);
			}
			if(node.getRight()!=null) {
				generateCodes(node.getRight(),level + 1,1);
			}
			return;
		}
		
		if(node.getValue()!=null) { 
			node.setCode(code);
			node.setLevel(level);
		}
		
		if(node.getLeft()!=null) {
			generateCodes(node.getLeft(),level + 1,code);
		}
		if(node.getRight()!=null) {
			generateCodes(node.getRight(),level + 1,code + ((long) 1 << level));
		}
	}

	public void printTree() {
		printTree(root,"");
	}
	
	private void printTree(HuffmanWordNode node, String tabs) {
		if(node.getRight()!=null) {
			printTree(node.getRight(),tabs + "\t");
		}

		if(node.getValue()!=null) {
			System.out.println(tabs + "+ value: " + node.getValue() + " weight: " + node.getWeight() + " code: "  + node.getCode() + " == " + ArchiveeByteUtils.convertToBitsString(node.getCode(),node.getLevel()));	
		} else {
			System.out.println(tabs + "* " + node.getValue() + " weight: " + node.getWeight());	
		}
		
		
		if(node.getLeft()!=null) {
			printTree(node.getLeft(),tabs + "\t");
		}
	}
	
	public void printCodes() {
		for(HuffmanWordNode node : nodes) {
			System.out.println(node.getValue() + " weight: " + node.getWeight() + " code: " + node.getCode() + " : " + ArchiveeByteUtils.convertToBitsString(node.getCode(),node.getLevel()));
		}
	}
	
	
	/**
	 * @return the root
	 */
	public HuffmanWordNode getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(HuffmanWordNode root) {
		this.root = root;
	}

	/**
	 * @return the treeSet
	 */
	public TreeSet<HuffmanWordNode> getTreeSet() {
		return treeSet;
	}

	/**
	 * @param treeSet the treeSet to set
	 */
	public void setTreeSet(TreeSet<HuffmanWordNode> treeSet) {
		this.treeSet = treeSet;
	}

	/**
	 * @return the nodes
	 */
	public TreeSet<HuffmanWordNode> getNodes() {
		return nodes;
	}
	
}
