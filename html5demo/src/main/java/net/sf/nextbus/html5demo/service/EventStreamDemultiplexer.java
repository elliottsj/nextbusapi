/**
 * *****************************************************************************
 * Copyright (C) 2013 by James R. Doyle
 *
 * This file is part of the NextBus® Livefeed Java Adapter (nblf4j). See the
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership and licensing.
 *
 * nblf4j is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 *
 * nblf4j is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UJMP; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 *
 * Usage of the NextBus Web Service and its data is subject to separate Terms
 * and Conditions of Use (License) available at:
 *
 * http://www.nextbus.com/xmlFeedDocs/NextBusXMLFeed.pdf
 *
 *
 * NextBus® is a registered trademark of Webtech Wireless Inc.
 *
 *****************************************************************************
 */
package net.sf.nextbus.html5demo.service;

import javax.ejb.Singleton;
import java.util.Map;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jrd
 */
@Singleton
public class EventStreamDemultiplexer {

    private final Logger log = LoggerFactory.getLogger(EventStreamDemultiplexer.class);

    /**
     * A Stream is associated with a unique Web Session and has a user-defined Selector
     * criterion that determines if traffic is destined for a given subscriber stream.
     * The Selector might be coded for geolocation reference, or content matching types,
     * or any other sort of personalization/localization criteria.
     */
    public class RegisteredStream {
        String webSessionId;
        IStreamSelector eventStreamSelector;
        ClientEventStream eventStream;
    }

    /**
     * The collection of subscribers
     */
    private Map<String, RegisteredStream> dispatch;

    public EventStreamDemultiplexer() {
        dispatch = new java.util.HashMap<String, RegisteredStream>();
    }

    /**
     * The Producer, or event source, feeds events to the demultiplexer using this method.
     * Each Stream then applies its Selector to determine if the event of it interested.
     * If so, the message is enqueued for that subscriber.
     */
    public void distributeEvents(Object newevent) {
        Iterator<RegisteredStream> streams = dispatch.values().iterator();
        int streamsDistributed = 0;
        long startTime = System.currentTimeMillis();
        while (streams.hasNext()) {
            RegisteredStream stream = streams.next();
            streamsDistributed++; 
            if (stream.eventStreamSelector.eventStreamMatches(newevent)) {
                stream.eventStream.addEvent(newevent);
                log.trace("+ event {} matched stream {} ", new Object[]{newevent, stream.webSessionId});
            } else {
                log.trace("- event {} did not match stream {} ", new Object[]{newevent, stream.webSessionId});
            }
        }
        long endTime = System.currentTimeMillis();
        log.debug("distributeEvents() took {} milliseconds to dispatch to [] listeners", new Object[]{endTime - startTime, streamsDistributed});
        // thread wait and notify happens here.       
    }

    /**
     * The Eventstream client registers itself to to the demux feed using this method.
     *
     * @param sessionId the web session ID of the client.
     * @param selector the client's content selector.
     * @param streamType the type of stream - Message Type or Message ID based.
     * @param waitTime the polling wait time for the consumer thread.
     * @return the stream associated with the given web session ID.
     */
    public synchronized ClientEventStream registerEventStream(String sessionId, IStreamSelector selector, ClientEventStream.StreamType streamType, Long waitTime) {
        if (dispatch.containsKey(sessionId)) {
            return dispatch.get(sessionId).eventStream;
        }
        // otherwise create a new registered stream.
        RegisteredStream s = new RegisteredStream();
        s.webSessionId = sessionId;
        s.eventStreamSelector = selector;
        s.eventStream = new ClientEventStream(streamType, waitTime);
        dispatch.put(sessionId, s);
        return s.eventStream;
    }

    /**
     * Removes an Event Stream from the dispatcher, i.e. when the end-users web session is nuked
     * and expunged.
     * @param sessionId web session id.  
     */
    public synchronized void removeEventStream(String sessionId) {
      RegisteredStream s = dispatch.get(sessionId);
      if (s==null) return;
      dispatch.remove(sessionId);
      log.info("removed event stream for session from singleton demux.");
      s.eventStream.quiesce();
      log.info("purged session scoped event stream.");
    }
    
    /**
     * Get the Event Stream for a given web session.
     * @param sessionId
     * @return the event stream for the given web session ID, null if there is no such event 
     * stream registered.
     */
    public synchronized ClientEventStream getEventStreamForSessionID(String sessionId) {
        RegisteredStream s = dispatch.get(sessionId);
        if (s == null) {
            return null;
        }
        return s.eventStream;
    }
   
    /**
     * Metric - get number of web session subscribers registered in the dispatcher. 
     * @return 
     */
    public int getSubscribers() {
        return dispatch.size();
    }
   
}
