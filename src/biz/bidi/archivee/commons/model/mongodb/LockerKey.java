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
 * @since Oct 26, 2012
 */
public class LockerKey {

	/**
	 * The threadId dataslot
	 */
	private long threadId;

	/**
	 * The component id
	 */
	private int componentId;

	/**
	 * @return the threadId
	 */
	public long getThreadId() {
		return threadId;
	}

	/**
	 * @param threadId the threadId to set
	 */
	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	/**
	 * @return the componentId
	 */
	public int getComponentId() {
		return componentId;
	}

	/**
	 * @param componentId the componentId to set
	 */
	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}
	
}
