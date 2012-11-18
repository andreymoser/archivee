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

import com.bea.common.security.jdkutils.WeaverUtil.Collections;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.huffman.HuffmanWordNode;
import biz.bidi.archivee.commons.model.huffman.HuffmanWordTree;
import biz.bidi.archivee.commons.model.mongodb.Dictionary;
import biz.bidi.archivee.commons.model.mongodb.DictionaryEntry;

/**
 * Byte utilities
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Oct 2, 2012
 */
public class ArchiveeByteUtils {

	private static ArchiveeByteUtils instance = new ArchiveeByteUtils();
	
	public static ArchiveeByteUtils getInstance() {
		return instance;
	}
	
	public static String convertToBitsString(long value) {
		return convertToBitsString(value, 8*8);
	}
	
	public static String convertToBitsString(long value, int lenght) {
		String bitstr = "";
		
		long val = 1;
		int byteCount = 0;
		
		for(int i=0; i<lenght; i++) {
			if(byteCount == 8) {
				bitstr += "|";
				byteCount = 0;
			}
			
			if((value & val) > 0) {
				bitstr += "1";
			} else {
				bitstr += "0";
			}
			
			val = val * 2;
			byteCount++;
		}
		
		return bitstr;
	}
	
	public static int getMaxBitsLength(long value) {
		int length = 0;
		
		long val = 1;
		
		for(int i=0; i<64; i++) {
			length = i;
			val = val * 2;
			
			if(val >= value) {
				length++;
				break;
			}
		}
		
		return length;
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

	public static Byte[] encode(ArrayList<String> values, HuffmanWordTree huffmanWordTree) throws ArchiveeException {
		ArrayList<Byte> bytes = new ArrayList<Byte>();

		int offset = 0;
		
		for(String str : values) {
			HuffmanWordNode node = huffmanWordTree.getNode(str);
			
			offset = ArchiveeByteUtils.getInstance().append(bytes, offset, node.getCode(), node.getLevel());
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
		
		bitsValue = value >> offset;
		bitsValue = bitsValue & mask;
		
		return bitsValue;
	}
	
	public static ArrayList<HuffmanWordNode> decode(Byte[] bytes, HuffmanWordTree huffmanWordTree) {
		ArrayList<HuffmanWordNode> nodes = new ArrayList<HuffmanWordNode>();
		
		ArrayList<Byte> bytesList = new ArrayList<Byte>();
		Collections.addAll(bytesList, bytes);
		
		int index = 0;
		int offset = 0;
		int index_aux = 0;
		int offset_aux = 0;
		
		Dictionary dictionary = new Dictionary();
		
		for(HuffmanWordNode node : huffmanWordTree.getNodes()) {
			DictionaryEntry dictionaryEntry = new DictionaryEntry();
			dictionaryEntry.setBitsLength(node.getLevel());
			dictionaryEntry.setBytes(node.getCode());
			dictionary.getEntries().put(node.getValue(), dictionaryEntry);
		}
		
		while(index < bytes.length) {
			Object object = ArchiveeByteUtils.getInstance().decode(bytesList,index,offset, (HashMap) dictionary.getEntries());

			String word = (String) object;
			
			if(word == null && index == bytes.length - 1) {
				break;
			}
			
			DictionaryEntry entry = dictionary.getEntries().get(word);
			
			index_aux = ArchiveeByteUtils.getInstance().nextDecodeIndex(index, offset, entry.getBitsLength());
			offset_aux = ArchiveeByteUtils.getInstance().nextDecodeOffset(index, offset, entry.getBitsLength());
			index = index_aux;
			offset = offset_aux;
			
			nodes.add(huffmanWordTree.getNode(word));
		}
		
		return nodes;
	}
	
	public int append(Object value, ArrayList<Byte> buffer, int offset, HashMap<Object,DictionaryEntry> dictionary) throws ArchiveeException {
		DictionaryEntry dictionaryEntry = dictionary.get(value);
		
		if(dictionaryEntry == null) {
			throw new ArchiveeException("Invalid object for dictionary ",this,value,dictionary);
		}
		
		return append(buffer, offset, dictionaryEntry.getBytes(), dictionaryEntry.getBitsLength());
	}
	
	public int append(String value, ArrayList<Byte> buffer, int offset, Dictionary dictionary) throws ArchiveeException {
		DictionaryEntry dictionaryEntry = dictionary.get(value);
		
		if(dictionaryEntry == null) {
			throw new ArchiveeException("Invalid object for dictionary ",this,value,dictionary);
		}
		
		return append(buffer, offset, dictionaryEntry.getBytes(), dictionaryEntry.getBitsLength());
	}

	public int append(ArrayList<Byte> buffer, int offset, long code, int length) throws ArchiveeException {
		int index = buffer.size() - 1;

		Byte byteValue = null;
		if(index >= 0 && offset != 0) {
			byteValue = buffer.get(index);
			buffer.remove(index);
		} else {
			byteValue = new Byte((byte) 0);
		}
		
		boolean added = false;
		
		int bits = length;
		int bitsHeader = 0;
		
		while(bits > 0) {
			
			int bitsAdded = 0;
			
			bitsAdded = 8 - offset;
			if(bitsAdded > bits) {
				bitsAdded = bits; 
			} 
			
			byteValue = (byte) ((byteValue | ((code >> bitsHeader) & 0xFF) << offset) & 0xFF) ;
			
			if((offset + bitsAdded) >= 8) {
				offset = (offset + bitsAdded) - 8;
				buffer.add(byteValue);
				byteValue = new Byte((byte) 0);
				added = true;
			} else {
				offset = offset + bitsAdded;
				added = false;
			}
			
			bits = bits - bitsAdded;
			bitsHeader = bitsHeader + bitsAdded;
		}
		
		if(!added) {
			buffer.add(byteValue);
		}
		
		return offset;
	}
	
	public static int nextDecodeIndex(int index, int offset, int bitsread) {
		int mod = bitsread/8;
		int remaining = bitsread - mod*8;
		
		if(mod > 0) {
			index = mod + index;
			if(offset + remaining >= 8) {
				index++;
			}
		} else {
			if(offset + bitsread >= 8) {
				index++;
			}
		}
		
		return index;
	}
	
	public static int nextDecodeOffset(int index, int offset, int bitsread) {
		int mod = bitsread/8;

		if(offset + bitsread >= 8) {
			int remaining = bitsread - mod*8; 
			
			if(offset + remaining >= 8) {
				if(offset + remaining == 8) {
					offset = 0;
				} else {
					offset = (offset + remaining) - 8; 
				}
			} else {
				offset = offset + remaining;
			}
		} else {
			offset = offset + bitsread;
		}
		
		return offset;
	}

	//TODO REVIEW
	public static Object decode(ArrayList<Byte> buffer, int index, int offset, HashMap<Object,DictionaryEntry> dictionary) {
		Object object = null;
		
		long value = 0;
		int bits = 0;
		int shift = 0;
		int size = 1;
			
		while(index < buffer.size()) {
			Byte b = buffer.get(index);
			
			value = value | (b & 0xFF) << shift;
			
			while(true) {
				DictionaryEntry entry = new DictionaryEntry();
				
				entry.setBitsLength(size);
				entry.setBytes(getBitsValue(value, offset, size));
				
				Object o = getUniqueObject(entry, dictionary); 
				if(o != null) {
					object = o;
					break;
				} else {
					bits++;
					size++;
				}
						
				if(bits > 8 || (shift == 0 && (offset + bits) >= 8)) {
					shift = shift + 8;
					break;
				}
			}
			
			if(object != null) {
				break;
			}
			
			index++;
		}
		
		return object;
	}
	
	public static long decode(ArrayList<Byte> buffer, int index, int offset, int length) {
		long value = 0;
		int mod = length/8;
		if(mod == 0) {
			mod = 1;
		}
		
		int remaining = 0;
		if(length > 8) {
			remaining = length - mod*8;
		} else {
			remaining = length;
		}
		
		if(offset + remaining >= 8) {
			mod++;
		}		
		
		for(int i=0; i < mod; i++) {
			Byte b = buffer.get(index + i);
			value = value | (b & 0xFF) << (8*i);
		}
		
		return getBitsValue(value, offset, length);
	}
	
	private static Object getUniqueObject(DictionaryEntry entry, HashMap<Object,DictionaryEntry> dictionary) {
		Object object = null;
		
		int found = 0;
		
		for(Object o : dictionary.keySet()) {
			DictionaryEntry entry2 = dictionary.get(o);
			if(entry.equals(entry2)) {
				object = o;
				found++;
				if(found > 1) {
					object = null;
					break;
				}
			}
		}
		
		return object;
	}
	
	public static String convertToBitsString(Byte[] bytes) {
		String bitstr = "";
		int count = 0;
		
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
			count++;
			if(count < bytes.length) {
				bitstr += "|";
			}
		}
		
		return bitstr;
	}
	
}
