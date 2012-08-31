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
package biz.bidi.test.jms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * Simple JMS test
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Aug 21, 2012
 */
public class JMSChat implements MessageListener {

	private TopicSession pubSession;
	private TopicSession subSession;
	private TopicPublisher publisher;
	private TopicConnection connection;
	private String username;

	/**
	 * 
	 */
	public JMSChat(String topicName, String connectionFactoryJNDI, String username, String password, String host, int port)
			throws Exception {
		// Obtain a JNDI connection
		Properties env = new Properties();
		env.put(Context.SECURITY_PRINCIPAL, username);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.INITIAL_CONTEXT_FACTORY,
		"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, "t3://" + host + ":" + port);
		
		// ... specify the JNDI properties specific to the vendor
		InitialContext jndi = new InitialContext(env);
		// Look up a JMS connection factory
		TopicConnectionFactory conFactory = (TopicConnectionFactory) jndi
				.lookup(connectionFactoryJNDI);
		// Create a JMS connection
		TopicConnection connection = conFactory.createTopicConnection(username,
				password);

		// Create two JMS session objects
		TopicSession pubSession = connection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		TopicSession subSession = connection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		// Look up a JMS topic
		Topic chatTopic = (Topic) jndi.lookup(topicName);

		// Create a JMS publisher and subscriber
		TopicPublisher publisher = pubSession.createPublisher(chatTopic);
		TopicSubscriber subscriber = subSession.createSubscriber(chatTopic);
		// Set a JMS message listener
		subscriber.setMessageListener(this);
		// Initialize the Chat application
		setConnection(connection);
		setPubSession(pubSession);
		setSubSession(subSession);
		setPublisher(publisher);
		setUsername(username);

		// Start the JMS connection; allows messages to be delivered
		connection.start();
	}

	/* Receive message from topic subscriber */
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			System.out.println("Received from subscriber: " + text);
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}
	}

	/* Create and send message using topic publisher */
	protected void writeMessage(String text) throws JMSException {
		TextMessage message = pubSession.createTextMessage();
		message.setText(text);
		publisher.publish(message);
		
		System.out.println("Published message. \n");
	}

	/* Close the JMS connection */
	public void close() throws JMSException {
		connection.close();
	}

	/* Run the Chat client */
	public static void main(String[] args) {
		try {
			String topicName = "";
			String username = "";
			String password = "";
			String connectionFactoryJNDI = "";
			
			if (args.length == 4) {
				topicName=args[0]; 
				username=args[1]; 
				password=args[2];
				connectionFactoryJNDI=args[3];
			} else {
				Scanner scanner = new Scanner(System.in);
				System.out.print("Topic name jndi: ");
				topicName = scanner.nextLine();
				System.out.print("Username: ");
				username = scanner.nextLine();
				System.out.print("Password: ");
				password = scanner.next();
				System.out.print("Connection factory jndi: ");
				connectionFactoryJNDI = scanner.next();
			}
				
			JMSChat chat = new JMSChat(topicName, connectionFactoryJNDI, username, password, "localhost", 7001);
			// Read from command line
			BufferedReader commandLine = new java.io.BufferedReader(
					new InputStreamReader(System.in));
			// Loop until the word "exit" is typed
			while (true) {
				String s = commandLine.readLine();
				if (s.equalsIgnoreCase("exit")) {
					chat.close(); // close down connection
					System.exit(0);// exit program
				} else
					chat.writeMessage(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the pubSession
	 */
	public TopicSession getPubSession() {
		return pubSession;
	}

	/**
	 * @param pubSession
	 *            the pubSession to set
	 */
	public void setPubSession(TopicSession pubSession) {
		this.pubSession = pubSession;
	}

	/**
	 * @return the subSession
	 */
	public TopicSession getSubSession() {
		return subSession;
	}

	/**
	 * @param subSession
	 *            the subSession to set
	 */
	public void setSubSession(TopicSession subSession) {
		this.subSession = subSession;
	}

	/**
	 * @return the publisher
	 */
	public TopicPublisher getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher
	 *            the publisher to set
	 */
	public void setPublisher(TopicPublisher publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return the connection
	 */
	public TopicConnection getConnection() {
		return connection;
	}

	/**
	 * @param connection
	 *            the connection to set
	 */
	public void setConnection(TopicConnection connection) {
		this.connection = connection;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
