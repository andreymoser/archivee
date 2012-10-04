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
package biz.bidi.archivee.components.logparser.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.interfaces.ILogParser;
import biz.bidi.archivee.commons.model.xml.ParserMessage;
import biz.bidi.archivee.commons.xml.ArchiveeXmlParser;
import biz.bidi.archivee.components.logparser.commons.LogParserManager;

@MessageDriven( activationConfig = {
		@ActivationConfigProperty(
					propertyName="destinationName",
					propertyValue="jms.archivee.inputQueue"
				)
})

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 11, 2012
 */
public class LogInputQueueMDB implements MessageListener {

	/**
	 * The logParser instance
	 */
	private ILogParser logParser;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message message) {
		if(message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			String xml = "";
			try {
				xml = textMessage.getText();
				logParser = LogParserManager.getInstance().getLogParser();
				logParser.parseLog((ParserMessage) ArchiveeXmlParser.convertoToObject(xml));
			} catch (JMSException e) {
				ArchiveeException.log(this,"Unable to read mdb text message",textMessage);
			} catch (ArchiveeException e) {
				ArchiveeException.log(this,"Unable to parse log line",xml,logParser);
			}
		} else {
			ArchiveeException.log(this,"Invalid mdb message, expected TextMessage",message);
		}
	}

}
