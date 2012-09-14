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
package biz.bidi.archivee.commons.model;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;

/**
 * Generic operator class
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 13, 2012
 * @deprecated
 */
public interface IOperator {

	/**
	 * Equal
	 * @return
	 * @throws ArchiveeException
	 */
	public IOperator eq() throws ArchiveeException;
	
	/**
	 * Not equal
	 * @return
	 * @throws ArchiveeException
	 */
	public IOperator ne() throws ArchiveeException;
	
	/**
	 * Exists
	 * @return
	 * @throws ArchiveeException
	 */
	public IOperator ex() throws ArchiveeException;
	
	/**
	 * greater than
	 * @return
	 * @throws ArchiveeException
	 */
	public IOperator gt() throws ArchiveeException;
	
	/**
	 * greater than or equal
	 * @return
	 * @throws ArchiveeException
	 */
	public IOperator gte() throws ArchiveeException;
	
	/**
	 * less than 
	 * @return
	 * @throws ArchiveeException
	 */
	public IOperator lt() throws ArchiveeException;

	/**
	 * less than or equal
	 * @return
	 * @throws ArchiveeException
	 */
	public IOperator lte() throws ArchiveeException;
	
	/**
	 * and
	 * @return
	 * @throws ArchiveeException
	 */
	public IOperator and() throws ArchiveeException;
	
	/**
	 * or
	 * @return
	 * @throws ArchiveeException
	 */
	public IOperator or() throws ArchiveeException;
	
	/**
	 * Get operator value
	 * @throws ArchiveeException
	 */
	public String getValue() throws ArchiveeException;
	
}
