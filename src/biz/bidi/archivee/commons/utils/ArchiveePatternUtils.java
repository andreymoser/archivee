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

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 14, 2012
 */
public class ArchiveePatternUtils {

	public static String createRegex(String s) {  
	    StringBuilder b = new StringBuilder();  
	    for(int i=0; i<s.length(); ++i) {  
	        char ch = s.charAt(i);  
	        if ("\\.^$|?*+[]{}()".indexOf(ch) != -1)  
	            b.append('\\').append(ch);  
	        else if (Character.isLetter(ch))  
	            b.append("[A-Za-z]");  
	        else if (Character.isDigit(ch))  
	            b.append("\\d");  
	        else  
	            b.append(ch);  
	    }  
	    return b.toString();  
	}
	
}
