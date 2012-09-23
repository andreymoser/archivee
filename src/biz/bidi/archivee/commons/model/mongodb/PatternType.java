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
package biz.bidi.archivee.commons.model.mongodb;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 16, 2012
 */
public enum PatternType {

	R("Root"),
	D("Date"),
	L("Level"),
	P("Pattern");
	
	public final String type;

	/**
	 * @param type
	 */
	private PatternType(String type) {
		this.type = type;
	}
	
	public static PatternType root() {
		return PatternType.R;
	}
	
	public static PatternType date() {
		return PatternType.D;
	}
	
	public static PatternType level() {
		return PatternType.L;
	}
	
	public static PatternType pattern() {
		return PatternType.P;
	}
		
}
