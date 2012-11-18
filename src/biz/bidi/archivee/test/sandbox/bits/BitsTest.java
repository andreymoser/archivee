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

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.huffman.HuffmanWordNode;
import biz.bidi.archivee.commons.model.huffman.HuffmanWordTree;
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
		
		ArrayList<String> words = new ArrayList<String>();
		words.add("a");
		words.add("a");
		words.add("a");
		words.add("a");
		words.add("a");
		words.add("a");
		words.add("a");
		words.add("a");
		words.add("a");
		words.add("a");
		words.add("a");
		words.add("a");
		words.add("a");
		words.add("a");
		words.add("b");
		words.add("b");
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
		words.add("f");
		words.add("f");
		words.add("f");
		words.add("f");
		words.add("g");
		words.add("e");
		words.add("e");
		words.add("h");
		words.add("i");
		words.add("i");
		words.add("i");
		words.add("i");
		words.add("j");
		words.add("j");
		words.add("k");
		words.add("k");
		words.add("k");
		words.add("k");
		words.add("k");
		words.add("q");
		words.add("r");
		words.add("t");
		words.add("t");
		words.add("y");
		words.add("w");
		words.add("x");
		words.add("1");
		words.add("2");
		words.add("3");
		words.add("4");
		words.add("5");
		words.add("6");
		words.add("7");
		words.add("8");
		words.add("9");
		words.add("aa");
		words.add("aa");
		words.add("aa");
		words.add("aa");
		words.add("aa");
		words.add("aa");
		words.add("aa");
		words.add("aa");
		words.add("aa");
		words.add("aa");
		words.add("aa");
		words.add("aa");
		words.add("aa");
		words.add("aa");
		words.add("bb");
		words.add("bb");
		words.add("bb");
		words.add("bb");
		words.add("aa");
		words.add("cc");
		words.add("cc");
		words.add("aa");
		words.add("aa");
		words.add("dd");
		words.add("dd");
		words.add("ff");
		words.add("ff");
		words.add("ff");
		words.add("ff");
		words.add("ff");
		words.add("gg");
		words.add("ee");
		words.add("ee");
		words.add("hh");
		words.add("ii");
		words.add("ii");
		words.add("ii");
		words.add("ii");
		words.add("jj");
		words.add("jj");
		words.add("kk");
		words.add("kk");
		words.add("kk");
		words.add("kk");
		words.add("kk");
		words.add("qq");
		words.add("rr");
		words.add("tt");
		words.add("tt");
		words.add("yy");
		words.add("ww");
		words.add("xx");
		words.add("11");
		words.add("22");
		words.add("33");
		words.add("44");
		words.add("55");
		words.add("66");
		words.add("77");
		words.add("88");
		words.add("99");
		words.add("aaa");
		words.add("aaa");
		words.add("aaa");
		words.add("aaa");
		words.add("aaa");
		words.add("aaa");
		words.add("aaa");
		words.add("aaa");
		words.add("aaa");
		words.add("aaa");
		words.add("aaa");
		words.add("aaa");
		words.add("aaa");
		words.add("aaa");
		words.add("bbb");
		words.add("bbb");
		words.add("bbb");
		words.add("bbb");
		words.add("aaa");
		words.add("ccc");
		words.add("ccc");
		words.add("aaa");
		words.add("aaa");
		words.add("ddd");
		words.add("ddd");
		words.add("fff");
		words.add("fff");
		words.add("fff");
		words.add("fff");
		words.add("fff");
		words.add("ggg");
		words.add("eee");
		words.add("eee");
		words.add("hhh");
		words.add("iii");
		words.add("iii");
		words.add("iii");
		words.add("iii");
		words.add("jjj");
		words.add("jjj");
		words.add("kkk");
		words.add("kkk");
		words.add("kkk");
		words.add("kkk");
		words.add("kkk");
		words.add("qqq");
		words.add("rrr");
		words.add("ttt");
		words.add("ttt");
		words.add("yyy");
		words.add("www");
		words.add("xxx");
		words.add("111");
		words.add("222");
		words.add("333");
		words.add("444");
		words.add("555");
		words.add("666");
		words.add("777");
		words.add("888");
		words.add("999");
		words.add("aaaa");
		words.add("aaaa");
		words.add("aaaa");
		words.add("aaaa");
		words.add("aaaa");
		words.add("aaaa");
		words.add("aaaa");
		words.add("aaaa");
		words.add("aaaa");
		words.add("aaaa");
		words.add("aaaa");
		words.add("aaaa");
		words.add("aaaa");
		words.add("aaaa");
		words.add("bbbb");
		words.add("bbbb");
		words.add("bbbb");
		words.add("bbbb");
		words.add("aaaa");
		words.add("cccc");
		words.add("cccc");
		words.add("aaaa");
		words.add("aaaa");
		words.add("dddd");
		words.add("dddd");
		words.add("ffff");
		words.add("ffff");
		words.add("ffff");
		words.add("ffff");
		words.add("ffff");
		words.add("gggg");
		words.add("eeee");
		words.add("eeee");
		words.add("hhhh");
		words.add("iiii");
		words.add("iiii");
		words.add("iiii");
		words.add("iiii");
		words.add("jjjj");
		words.add("jjjj");
		words.add("kkkk");
		words.add("kkkk");
		words.add("kkkk");
		words.add("kkkk");
		words.add("kkkk");
		words.add("qqqq");
		words.add("rrrr");
		words.add("tttt");
		words.add("tttt");
		words.add("yyyy");
		words.add("wwww");
		words.add("xxxx");
		words.add("1111");
		words.add("2222");
		words.add("3333");
		words.add("4444");
		words.add("5555");
		words.add("6666");
		words.add("7777");
		words.add("8888");
		words.add("9999");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("bbbbb");
		words.add("bbbbb");
		words.add("bbbbb");
		words.add("bbbbb");
		words.add("aaaaa");
		words.add("ccccc");
		words.add("ccccc");
		words.add("aaaaa");
		words.add("aaaaa");
		words.add("ddddd");
		words.add("ddddd");
		words.add("fffff");
		words.add("fffff");
		words.add("fffff");
		words.add("fffff");
		words.add("fffff");
		words.add("ggggg");
		words.add("eeeee");
		words.add("eeeee");
		words.add("hhhhh");
		words.add("iiiii");
		words.add("iiiii");
		words.add("iiiii");
		words.add("iiiii");
		words.add("jjjjj");
		words.add("jjjjj");
		words.add("kkkkk");
		words.add("kkkkk");
		words.add("kkkkk");
		words.add("kkkkk");
		words.add("kkkkk");
		words.add("qqqqq");
		words.add("rrrrr");
		words.add("ttttt");
		words.add("ttttt");
		words.add("yyyyy");
		words.add("wwwww");
		words.add("xxxxx");
		words.add("11111");
		words.add("22222");
		words.add("33333");
		words.add("44444");
		words.add("55555");
		words.add("66666");
		words.add("77777");
		words.add("88888");
		words.add("99999");
		
		
		HuffmanWordTree huffmanWordTree = new HuffmanWordTree();
		huffmanWordTree.buildTree(words);
		huffmanWordTree.printTree();
		huffmanWordTree.printCodes();
		
		
		String stressWord = null;
		
		for(HuffmanWordNode node : huffmanWordTree.getNodes()) {
			if(node.getLevel() > 8) {
				stressWord = node.getValue();
				break;
			}
		}
		
		ArrayList<String> data = new ArrayList<String>();
		data.add("e");
		data.add("a");
		data.add("c");
		data.add("b");
		data.add("1");
		if(stressWord != null) {
			data.add(stressWord);
		}
		data.add("k");
		data.add("e");
		data.add("a");
		if(stressWord != null) {
			data.add(stressWord);
		}
		data.add("2");
		data.add("3");
		data.add("4");
		if(stressWord != null) {
			data.add(stressWord);
		}
		data.add("5");
		data.add("h");
		if(stressWord != null) {
			data.add(stressWord);
		}
		data.add("i");
		data.add("a");

		try {
			String encoded = data.toString();
			
			Byte[] dataEncoded = ArchiveeByteUtils.encode(data, huffmanWordTree);
			
			String bitsEncoded = ArchiveeByteUtils.convertToBitsString(dataEncoded);
			
			ArrayList<HuffmanWordNode> nodesDecoded = ArchiveeByteUtils.decode(dataEncoded,huffmanWordTree);
			
			System.out.println("encoded: " + encoded + " : " + bitsEncoded);
			System.out.println("decoded: " + nodesDecoded);
		} catch (ArchiveeException e) {
			e.printStackTrace();
		}
		
	}

}
