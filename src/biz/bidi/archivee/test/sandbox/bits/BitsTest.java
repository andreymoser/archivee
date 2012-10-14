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
package biz.bidi.archivee.test.sandbox.bits;

import java.util.ArrayList;
import java.util.HashMap;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.huffman.HuffmanWordTree;
import biz.bidi.archivee.commons.model.mongodb.DictionaryEntry;
import biz.bidi.archivee.commons.utils.ArchiveeByteUtils;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Oct 2, 2012
 */
public class BitsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i=0; i < 100; i++) {
			System.out.println(i + " : " + ArchiveeByteUtils.convertToBitsString(i));
		}
		
		ArrayList<String> words = new ArrayList<String>();
		words.add("a");
		words.add("a");
		words.add("a");
		words.add("b");
		words.add("b");
		words.add("a");
		words.add("c");
		words.add("c");
		words.add("a");
		words.add("a");
		words.add("d");
		words.add("d");
		words.add("f");
		words.add("g");
		words.add("e");
		words.add("e");
		words.add("h");
		
		HuffmanWordTree huffmanWordTree = new HuffmanWordTree();
		huffmanWordTree.buildTree(words);
		huffmanWordTree.printTree();
		huffmanWordTree.printCodes();
		
		ArrayList<String> data = new ArrayList<String>();
		data.add("e");
		data.add("a");
		data.add("c");
		data.add("b");
		data.add("e");
		data.add("a");
		data.add("h");
		data.add("a");

		System.out.println("encoded: " + data.toString() + " : " + ArchiveeByteUtils.convertToBitsString(ArchiveeByteUtils.encode(data, huffmanWordTree)));
		System.out.println("decoded: " + ArchiveeByteUtils.decode(ArchiveeByteUtils.encode(data, huffmanWordTree),huffmanWordTree));
		
		HashMap<String,DictionaryEntry> dictionary = new HashMap<String, DictionaryEntry>();
		DictionaryEntry de = new DictionaryEntry();
		de.setBitsLength(2);
		de.setBytes(0);
		dictionary.put("a", de);
		
		DictionaryEntry de2 = new DictionaryEntry();
		de2.setBitsLength(3);
		de2.setBytes(1);
		dictionary.put("b", de2);
		
		DictionaryEntry de3 = new DictionaryEntry();
		de3.setBitsLength(3);
		de3.setBytes(5);
		dictionary.put("c", de3);
		
		ArrayList<Byte> buffer = new ArrayList<Byte>();
		
		int offset = 0;
		try {
			offset = ArchiveeByteUtils.append((Object) "a", buffer, offset, (HashMap) dictionary);
			offset = ArchiveeByteUtils.append((Object) "b", buffer, offset, (HashMap) dictionary);
			offset = ArchiveeByteUtils.append((Object) "c", buffer, offset, (HashMap) dictionary);
			offset = ArchiveeByteUtils.append((Object) "a", buffer, offset, (HashMap) dictionary);
			offset = ArchiveeByteUtils.append((Object) "b", buffer, offset, (HashMap) dictionary);
			offset = ArchiveeByteUtils.append((Object) "a", buffer, offset, (HashMap) dictionary);
			offset = ArchiveeByteUtils.append((Object) "b", buffer, offset, (HashMap) dictionary);
			offset = ArchiveeByteUtils.append((Object) "c", buffer, offset, (HashMap) dictionary);
			offset = ArchiveeByteUtils.append((Object) "c", buffer, offset, (HashMap) dictionary);
		} catch (ArchiveeException e) {
			e.printStackTrace();
		}
		
		System.out.println(ArchiveeByteUtils.convertToBitsString(buffer.toArray(new Byte[buffer.size()])));
		
		
		System.out.println(ArchiveeByteUtils.decode(buffer,0,0,(HashMap) dictionary));
		System.out.println(ArchiveeByteUtils.decode(buffer,0,2,(HashMap) dictionary));
		System.out.println(ArchiveeByteUtils.decode(buffer,0,5,(HashMap) dictionary));
		System.out.println(ArchiveeByteUtils.decode(buffer,1,0,(HashMap) dictionary));
		System.out.println(ArchiveeByteUtils.decode(buffer,1,2,(HashMap) dictionary));
		System.out.println(ArchiveeByteUtils.decode(buffer,1,5,(HashMap) dictionary));
		System.out.println(ArchiveeByteUtils.decode(buffer,1,7,(HashMap) dictionary));
		System.out.println(ArchiveeByteUtils.decode(buffer,2,2,(HashMap) dictionary));
		System.out.println(ArchiveeByteUtils.decode(buffer,2,5,(HashMap) dictionary));
		
	}

}
