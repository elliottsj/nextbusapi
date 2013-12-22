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
import net.sf.nextbus.html5demo.Geoselector;
import javax.ejb.Stateful;
import javax.ejb.Remove;
import javax.ejb.EJB;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
/**
 *
 * @author jrd
 */
@Stateful
public class RadarService implements Serializable {

    private final Logger log = LoggerFactory.getLogger(RadarService.class);
    
    @EJB
    private EventStreamDemultiplexer demux;

    public ClientEventStream eventStream;
    public Geoselector selector;
    
    public void registerWebSessionId(String arg) {
        eventStream = demux.registerEventStream(arg, selector, ClientEventStream.StreamType.MessageType, 1000L);
    }

    public Geoselector getSelector() {
        return selector;
    }
    
    public ClientEventStream getEventStream() {
        if (eventStream == null) {
            throw new IllegalArgumentException("No event stream create. Call SFSB registerWebSessionId() method before calling this method.");
        }
        return eventStream;
    }
    
    @PostConstruct
    public void init() {
        selector = new Geoselector();
        log.info("created SFSB.");
    }

    @Remove
    public void close() {
        log.info("closing SFSB.");
        String sessionId = eventStream.getId();
        demux.removeEventStream(sessionId);
    }

}
