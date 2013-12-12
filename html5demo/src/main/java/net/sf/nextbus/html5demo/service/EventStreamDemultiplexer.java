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

    public class RegisteredStream {

        String webSessionId;
        IStreamSelector eventStreamSelector;
        ClientEventStream eventStream;
    }

    private Map<String, RegisteredStream> dispatch;

    public EventStreamDemultiplexer() {
        dispatch = new java.util.HashMap<String, RegisteredStream>();
    }

    /**
     * The event stream producer distributed events to its many stream
     * subscribers here
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
     * The Event Stream client (session-scoped bean for a servlet) registers
     * itself here.
     *
     * @param sessionId
     * @param selector
     * @param streamType
     * @param waitTime
     * @return
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
     * 
     * @param sessionId 
     */
    public synchronized void removeEventStream(String sessionId) {
      RegisteredStream s = dispatch.get(sessionId);
      dispatch.remove(sessionId);
      s.eventStream.purgeAll();
    }
    /**
     *
     * @param sessionId
     * @return
     */
    public synchronized ClientEventStream getEventStreamForSessionID(String sessionId) {
        RegisteredStream s = dispatch.get(sessionId);
        if (s == null) {
            return null;
        }
        return s.eventStream;
    }
}
