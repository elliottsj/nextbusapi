/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.nextbus.html5demo.service;

import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Topic;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author jrd
 */
@MessageDriven
public class EventListener implements MessageListener {
    private static Logger log = LoggerFactory.getLogger(EventListener.class);
    
    @Resource
    private ConnectionFactory connectionFactory;

    @Resource(name = "NextBusTopic")
    private Topic answerQueue;

    @Override
    public void onMessage(Message message) {
        try {

            //final TextMessage textMessage = (TextMessage) message;
            //final String question = textMessage.getText();
            log.info("onMessage()");
            message.acknowledge();
            
        } catch (JMSException e) {
            throw new IllegalStateException(e);
        }
    }
}
