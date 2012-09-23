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
package biz.bidi.archivee.commons.xml;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.xml.IXmlObject;

import com.thoughtworks.xstream.XStream;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 23, 2012
 */
public class ArchiveeXmlParser {

	/**
	 * The singleton xstream instance
	 */
	private static final XStream xstream = new XStream();
	
	/**
	 * Converts object to a string xml
	 * @param xml
	 * @param object
	 * @return IXmlObject
	 * @throws ArchiveeException
	 */
	public static String convertoToXml(IXmlObject object) throws ArchiveeException {
		String xml = "";
		
		try {
			xml = xstream.toXML(object);
		} catch (Exception e) {
			throw new ArchiveeException(e,"Error while trying to convert object to xml",object);
		}
		
		return xml;
	}
	
	/**
	 * Converts a xml string to an object
	 * @param xml
	 * @param object
	 * @return IXmlObject
	 * @throws ArchiveeException
	 */
	public static IXmlObject convertoToObject(String xml) throws ArchiveeException {
		IXmlObject object = null;
		try {
			object = (IXmlObject) xstream.fromXML(xml);
		} catch (Exception e) {
			throw new ArchiveeException(e,"Error while trying to convert xml to object",xml,object);
		}
		
		return object;
	}
}
