/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.nextbus.html5demo.service;

import javax.annotation.Resource;
import javax.ejb.EJB;
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
import javax.jms.ObjectMessage;
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

    @Resource(name = "nextbus")
    private Topic nextbus;
    
    @EJB
    private EventStreamDemultiplexer demux;

    @Override
    public void onMessage(Message message) {
        try {
            log.info("In onMessage()");
            if (message instanceof ObjectMessage) {
                ObjectMessage om = (ObjectMessage) message;
                if (om.getObject() instanceof net.sf.nextbus.publicxmlfeed.domain.VehicleLocation) {
                   demux.distributeEvents(message);
                   message.acknowledge();
                   log.info(" --> acked.");
                   return;
                }
            }
            log.info("skipping message - not of type VehicleLocation");
            message.acknowledge();
        } catch (JMSException e) {
            log.error("in MDB",e);
            throw new IllegalStateException(e);
        }
    }
}
