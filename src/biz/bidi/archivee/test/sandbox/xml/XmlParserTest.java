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
package biz.bidi.archivee.test.sandbox.xml;

import java.util.Date;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.xml.ParserMessage;
import biz.bidi.archivee.commons.utils.ArchiveeDateUtils;
import biz.bidi.archivee.commons.xml.ArchiveeXmlParser;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 23, 2012
 */
public class XmlParserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ParserMessage message = new ParserMessage();
		
		message.setDate(ArchiveeDateUtils.convertDateToString(new Date()));
		message.setLevel("INFO");
		message.setMessage("message content");

		String xml = "";
		try {
			xml = ArchiveeXmlParser.convertoToXml(message);
			System.out.println(xml);
		} catch (ArchiveeException e) {
			ArchiveeException.log(e, "Error while converting to xml", message);
		}
		
		try {
			ParserMessage message2 = (ParserMessage) ArchiveeXmlParser.convertoToObject(xml);
			
			System.out.println("date : " + message2.getDate());
			System.out.println("level : " + message2.getLevel());
			System.out.println("message : " + message2.getMessage());
		} catch (ArchiveeException e) {
			ArchiveeException.log(e, "Error while converting to xml", message);
		}
	}

}
