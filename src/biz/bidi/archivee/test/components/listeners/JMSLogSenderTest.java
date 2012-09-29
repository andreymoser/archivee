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
package biz.bidi.archivee.test.components.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import biz.bidi.archivee.commons.jms.senders.JMSLogParserSender;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 9, 2012
 */
public class JMSLogSenderTest {

	private static JMSLogParserSender logSender;
	
	/**
	 * Test method for {@link biz.bidi.archivee.commons.jms.senders.JMSLogParserSender#JMSLogSender()}.
	 */
	@Test
	public final void testJMSLogSender() {
		logSender = new JMSLogParserSender();
		assertNotNull(logSender);
	}

	/**
	 * Test method for {@link biz.bidi.archivee.commons.jms.senders.JMSLogParserSender#sendLogLine(java.lang.String)}.
	 */
	@Test
	public final void testSendLogLine() {
		//TODO test jms
		//fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link biz.bidi.archivee.commons.jms.senders.JMSLogParserSender#loadProperties(java.lang.String)}.
	 */
	@Test
	public final void testLoadProperties() {
		assertEquals("system", logSender.getConnectionData().getUsername());
		assertEquals("weblogic12", logSender.getConnectionData().getPassword());
		assertEquals("weblogic.jndi.WLInitialContextFactory", logSender.getConnectionData().getConnectionFactoryClassName());
		assertEquals("localhost", logSender.getConnectionData().getHost());
		assertEquals(7001, logSender.getConnectionData().getPort());
		assertEquals("jms.archivee.inputQueue.connection.factory", logSender.getConnectionData().getConnectionFactoryJNDI());
		assertEquals("jms.archivee.inputQueue", logSender.getConnectionData().getConnectionJNDI());
	}

}
