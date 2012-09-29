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
package biz.bidi.archivee.commons.jms;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 9, 2012
 */
@SuppressWarnings("unused")
public abstract class ArchiveeJMSQueue extends ArchiveeJMSGeneric {

	protected QueueSession queueSession;
	protected QueueSender sender;
	protected QueueConnection connection;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.jms.IArchiveeMessaging#open()
	 */
	@Override
	public void open() throws ArchiveeException {
		try {
			Properties env = new Properties();
			env.put(Context.SECURITY_PRINCIPAL, connectionData.getUsername());
			env.put(Context.SECURITY_CREDENTIALS, connectionData.getPassword());
			env.put(Context.INITIAL_CONTEXT_FACTORY, connectionData.getConnectionFactoryClassName());
			env.put(Context.PROVIDER_URL, "t3://" + connectionData.getHost() + ":" + this.getConnectionData().getPort());
			InitialContext jndi = new InitialContext(env);
			
			QueueConnectionFactory connectionFactory = 
					(QueueConnectionFactory) jndi.lookup(connectionData.getConnectionFactoryJNDI());
			Queue queue = (Queue) jndi.lookup(connectionData.getConnectionJNDI());
			
			connection = connectionFactory.createQueueConnection(connectionData.getUsername(),connectionData.getPassword());

			queueSession = connection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
			sender = queueSession.createSender(queue);
			
			connection.start();
		} catch (JMSException e) {
			throw new ArchiveeException(e,"Unable to start JMS Queue connection",connectionData,this,connection);
		} catch (NamingException e) {
			throw new ArchiveeException(e,"Unable to start JMS Queue connection by invalid connection data or unresolved application server",connectionData,this,connection);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.jms.IArchiveeMessaging#send(java.lang.String)
	 */
	@Override
	public void send(String message) throws ArchiveeException {
		TextMessage textMessage = null;
		try {
			textMessage = queueSession.createTextMessage();
			textMessage.setText(message);
			sender.send(textMessage);
		} catch (JMSException e) {
			throw new ArchiveeException(e,"Unable to send message to JMS queue",connectionData,textMessage,this);			
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.jms.IArchiveeMessaging#close()
	 */
	@Override
	public void close() throws ArchiveeException {
		try {
			connection.close();
		} catch (JMSException e) {
			throw new ArchiveeException(e,"Unable to close JMS queue connection",connectionData,this);			
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		this.close();
		
		super.finalize();
	}

}
