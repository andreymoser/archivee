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

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 12, 2012
 */
public class TestMongodb {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//best to use (Mongo, String) method, where Mongo is a singleton.
		Datastore ds = new Morphia().createDatastore("archivee"); 

		//at application start
		//map classes before calling with morphia map* methods
		ds.ensureIndexes(); //creates indexes from @Index annotations in your entities
		ds.ensureCaps(); //creates capped collections from @Entity

		for(TestEntity entity : ds.find(TestEntity.class)) {
			System.out.println("Deleted : " + entity.name);
			ds.delete(entity);
		}

		TestEntity e = new TestEntity();
		e.name = "andrey";
		e.value = 10000;
		e.moreValues = new String[]{"a","b","c","d"};
		e.list = new ArrayList<String>();
		e.list.add("test");
		e.list.add("test");
		e.list.add("test");
		e.list.add("test");
		e.list.add("test");
		e.mapValues = new HashMap<String, Integer>();
		e.mapValues.put("100", 123);
		e.mapValues.put("200", 4235432);
		e.mapValues.put("300", 23452345);
		e.mapValues.put("400", 234523452);
		
		e.bytes = "test bytes array".getBytes(); 
				
		TestEntity e2 = new TestEntity();
		e2.name = "ciclano";
		e2.value = -100000000;
		e2.moreValues = new String[]{"a","b","c","d","e"};
		
		ds.save(e);
		ds.save(e2);
		
		e.name = "andrey";
		e.value = -400000000;
		ds.save(e);
		
		
		long ellpasedTime = System.currentTimeMillis();
		for(TestEntity entity : ds.find(TestEntity.class,"name","andrey")) {
			System.out.print("Found : " + entity.name);
			if(entity.bytes != null) {
				System.out.print(" bytes : " + new String(entity.bytes));
			}
			System.out.println("");
		}
		
		ellpasedTime = System.currentTimeMillis() - ellpasedTime; 
		System.out.println("Ellapsed time (ms): " + ellpasedTime);
	}

}
