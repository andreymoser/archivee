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

import java.util.ArrayList;
import java.util.HashMap;

import org.bson.types.ObjectId;

import biz.bidi.archivee.commons.model.huffman.HuffmanObjectIdTree;
import biz.bidi.archivee.commons.model.huffman.HuffmanWordNode;
import biz.bidi.archivee.commons.model.huffman.HuffmanWordTree;
import biz.bidi.archivee.commons.model.mongodb.TemplateDictionary;

/**
 * Byte utilities
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Oct 2, 2012
 */
public class ArchiveeByteUtils {

	public static String convertToBitsString(long value) {
		return convertToBitsString(value, 8*8);
	}
	
	public static String convertToBitsString(long value, int lenght) {
		String bitstr = "";
		
		long val = 1;
		
		for(int i=0; i<lenght; i++) {
			if((value & val) > 0) {
				bitstr += "1";
			} else {
				bitstr += "0";
			}
			
//			bitstr += " " + (value & val) + " ";
			
			val = val * 2;
		}
		
		return bitstr;
	}

	public static ArrayList<Long> getUniqueBytes(long seed) {
		ArrayList<Long> values = new ArrayList<Long>();
		
		values.add(seed);
		
		for(long i=seed+1; i < 100; i++) {
			
			boolean add = true;
			for(Long value : values) {
				long val = 1;
				for(int j=0; j<8*8; j++) {
					if(value >= val && val > 1) {
						break;
					}
					if((value & val) == (i & val)) {
						add = false;
					} 
					val = val * 2;
				}				
			}
			
			if(add) {
				values.add(i);
			}
		}
		
		return values;
	}

	public static Byte[] encode(ArrayList<String> values, HuffmanWordTree huffmanWordTree) {
		ArrayList<Byte> bytes = new ArrayList<Byte>();

		int offset = 0;
		byte value = 0;
		
		for(String str : values) {
			HuffmanWordNode node = huffmanWordTree.getNode(str);
			int tail = node.getLevel();
			int bits = 0;
			while(true) {
				if(offset == 0) {
					value = (byte) (node.getCode() >> bits);
				} else {
					value = (byte) (value | (byte) ((node.getCode() >> bits) << offset));
				}
				int shift = (tail>8?8:tail);
				offset += shift;
				tail = tail - shift;
				
				if(offset >= 8) {
					offset = 0;
					bytes.add(value);
					value = 0;
				}
				
				if(tail <= 0) {
					break;
				}
				bits = bits + 8;
			}
		}
		
		if(value != 0) {
			bytes.add(value);
		}
		
		return bytes.toArray(new Byte[bytes.size()]);
	}

	public static long getBitsValue(long value, int offset, int size) {
		long bitsValue = 0;
		
		long mask = 1;
		long val = 1;
		for(int i=1; i < size; i++) {
			val = val * 2;
			mask = mask + val;
		}
		
		bitsValue = (byte)((value >> offset) & 0xFF);
		bitsValue = (byte)(bitsValue & mask);
		
		return bitsValue;
	}
	
	
	public static ArrayList<HuffmanWordNode> decode(Byte[] bytes, HuffmanWordTree huffmanWordTree) {
		ArrayList<HuffmanWordNode> nodes = new ArrayList<HuffmanWordNode>();
		
		int offset = 0;
		long value = 0;
		int bits = 0;
		for(Byte b : bytes) {
			
			value = (getBitsValue(value, offset, bits) << 8) | b; 
			offset = 0;
			
			while(true) {
				HuffmanWordNode node = huffmanWordTree.getUniqueNode(getBitsValue(value, offset, bits + 1),bits + 1); 
				if(node != null) {
					nodes.add(node);
					offset = offset + bits + 1;
					bits = 0;
					value = b;
				} else {
					bits++;
				}
						
				if(bits > 8 || offset >= 8) {
					break;
				}
			}
		}
		
		return nodes;
	}
	
	public static long append(Object value, ArrayList<Byte> buffer, long bitOffset, HashMap<Object,Long> dictionary) {
		return 0;
	}

	public Object next(ArrayList<Byte> buffer, long bitOffset, HashMap<Object,Long> dictionary) {
		return null;
	}
	
	public long getBitLength(long value) {
		//TODO
		return 0;
	}
	
	public static String convertToBitsString(Byte[] bytes) {
		String bitstr = "";
		
		for(Byte b : bytes) {
			int val = 1;
			for(int i=0; i<8; i++) {
				if((b & val) > 0) {
					bitstr += "1";
				} else {
					bitstr += "0";
				}
				val = val * 2;
			}
		}
		
		return bitstr;
	}
	
}
