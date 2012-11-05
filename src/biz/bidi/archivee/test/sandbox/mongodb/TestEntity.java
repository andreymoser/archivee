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
package biz.bidi.archivee.test.sandbox.mongodb;

import java.util.ArrayList;
import java.util.HashMap;

import biz.bidi.archivee.commons.model.mongodb.IEntity;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 12, 2012
 */
@Entity("test")
public class TestEntity implements IEntity<String> {
	
	  @Id
	  public String name;
	  
	  public int value;
	  
	  public String[] moreValues;
	  
	  public ArrayList<String> list;
	  
	  public HashMap<String,Integer> mapValues;
	  
	  byte[] bytes;

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.model.mongodb.IEntity#getId()
	 */
	@Override
	public String getId() {
		return this.name;
	}
}
