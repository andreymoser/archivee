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
package biz.bidi.archivee.commons.dao.mongodb;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.model.IOperator;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 13, 2012
 * @deprecated
 */
public enum ArchiveeMongodbOperators implements IOperator {

	eq("="),
	ne("!="),
	lt("<"),
	lte("<="),
	gt(">"),
	gte(">="),
	and("and"),
	or("or");

	public final String value;
	
	/**
	 * @param value
	 */
	ArchiveeMongodbOperators(String value) {
		this.value = value;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.model.IOperator#eq()
	 */
	@Override
	public IOperator eq() throws ArchiveeException {
		return eq;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.model.IOperator#ne()
	 */
	@Override
	public IOperator ne() throws ArchiveeException {
		return ne;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.model.IOperator#ex()
	 */
	@Override
	public IOperator ex() throws ArchiveeException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.model.IOperator#gt()
	 */
	@Override
	public IOperator gt() throws ArchiveeException {
		return gt;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.model.IOperator#gte()
	 */
	@Override
	public IOperator gte() throws ArchiveeException {
		return gte;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.model.IOperator#lt()
	 */
	@Override
	public IOperator lt() throws ArchiveeException {
		return lt;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.model.IOperator#lte()
	 */
	@Override
	public IOperator lte() throws ArchiveeException {
		return lte;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.model.IOperator#and()
	 */
	@Override
	public IOperator and() throws ArchiveeException {
		return and;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.model.IOperator#or()
	 */
	@Override
	public IOperator or() throws ArchiveeException {
		return or;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.model.IOperator#getValue()
	 */
	@Override
	public String getValue() throws ArchiveeException {
		return value;
	}
	
}
