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
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import org.bson.types.ObjectId;

import biz.bidi.archivee.commons.utils.ArchiveeByteUtils;

/**
 * 
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Oct 8, 2012
 */
public class HuffmanObjectIdTree {

	private HuffmanObjectIdNode root;
	
	private TreeSet<HuffmanObjectIdNode> treeSet;
	
	private TreeSet<HuffmanObjectIdNode> nodes;
	
	public void buildTree(ArrayList<ObjectId> values) {
		// find out weight for all symbols		
		HashMap<ObjectId, Integer> refDictionary = new HashMap<ObjectId, Integer>();
		
		for(ObjectId value : values) {
			Integer weight = 0;
			if(refDictionary.containsKey(value)) {				
				weight = refDictionary.get(value);
			} 
			weight++;
			refDictionary.put(value, weight);
		}
		
		buildTree(refDictionary);
	}
	
	public void buildTree(HashMap<ObjectId, Integer> entries) {
		nodes = new TreeSet<HuffmanObjectIdNode>(new Comparator<HuffmanObjectIdNode>() {
			@Override
			public int compare(HuffmanObjectIdNode o1, HuffmanObjectIdNode o2) {
				return o1.getWeight()==o2.getWeight()?o2.getValue().compareTo(o1.getValue()):(o1.getWeight()>o2.getWeight()?-1:1);
			}
		});
		
		// builds priority queue
		treeSet = new TreeSet<HuffmanObjectIdNode>(new Comparator<HuffmanObjectIdNode>() {
			@Override
			public int compare(HuffmanObjectIdNode o1, HuffmanObjectIdNode o2) {
				if(o1.getWeight()!=o2.getWeight()) {
					return o1.getWeight()>o2.getWeight()?1:-1;
				}
				
				if(o1.getValue()==null && o2.getValue()==null) {
					return o1.hashCode() > o2.hashCode() ? 1 : -1;
				}
				
				if(o1.getValue()!=null && o2.getValue()==null) {
					return 1;
				}
				
				if(o1.getValue()==null && o2.getValue()!=null) {
					return -1;
				}

				if(o1.getValue()!=null && o2.getValue()!=null) {
					return o1.getValue().compareTo(o2.getValue());
				}
				
				return o1.hashCode() > o2.hashCode() ? 1 : -1;
			}
		});
		
		for(ObjectId value : entries.keySet()) {
			int weight = entries.get(value);
			
			HuffmanObjectIdNode node = new HuffmanObjectIdNode();
			node.setWeight(weight);
			node.setValue(value);
			
			treeSet.add(node);
		}
		
		System.out.println();
		
		// generates the huffman tree
		while(treeSet.size() > 1) {
			HuffmanObjectIdNode right = treeSet.pollFirst();
			HuffmanObjectIdNode left = treeSet.pollFirst();
			HuffmanObjectIdNode parent = new HuffmanObjectIdNode();
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
		
		generateCodes(root,0,0);
	}

	public HuffmanObjectIdNode getNode(ObjectId value) {
		HuffmanObjectIdNode found = null;
		
		for(HuffmanObjectIdNode node : nodes) {
			if(node.getValue().equals(value)) {
				found = node;
				break;
			}
		}
		
		return found;
	}
	
	public HuffmanObjectIdNode getNode(long code) {
		HuffmanObjectIdNode found = null;
		
		for(HuffmanObjectIdNode node : nodes) {
			if(node.getCode() == code) {
				found = node;
				break;
			}
		}
		
		return found;
	}
	
	public HuffmanObjectIdNode getUniqueNode(long code,int length) {
		HuffmanObjectIdNode found = null;
		
		int count = 0;
		
		for(HuffmanObjectIdNode node : nodes) {
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
	
	private void generateCodes(HuffmanObjectIdNode node, int level, long code) {
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
	
	private void printTree(HuffmanObjectIdNode node, String tabs) {
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
		for(HuffmanObjectIdNode node : nodes) {
			System.out.println(node.getValue() + " weight: " + node.getWeight() + " code: " + node.getCode() + " : "+ ArchiveeByteUtils.convertToBitsString(node.getCode(),node.getLevel()));
		}
	}
	
	
	/**
	 * @return the root
	 */
	public HuffmanObjectIdNode getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(HuffmanObjectIdNode root) {
		this.root = root;
	}

	/**
	 * @return the treeSet
	 */
	public TreeSet<HuffmanObjectIdNode> getTreeSet() {
		return treeSet;
	}

	/**
	 * @param treeSet the treeSet to set
	 */
	public void setTreeSet(TreeSet<HuffmanObjectIdNode> treeSet) {
		this.treeSet = treeSet;
	}

	/**
	 * @return the nodes
	 */
	public TreeSet<HuffmanObjectIdNode> getNodes() {
		return nodes;
	}
	
}
