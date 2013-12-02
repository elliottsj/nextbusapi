/*******************************************************************************
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
 * Usage of the NextBus Web Service and its data is subject to separate
 * Terms and Conditions of Use (License) available at:
 * 
 *      http://www.nextbus.com/xmlFeedDocs/NextBusXMLFeed.pdf
 * 
 * 
 * NextBus® is a registered trademark of Webtech Wireless Inc.
 *
 ******************************************************************************/
package net.sf.nextbus.html5demo;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import javax.servlet.http.HttpSessionBindingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSessionBindingListener;
import net.sf.nextbus.html5demo.service.ClientEventStream;
/**
 * Web Session context for the Geofeed POC.  All web session objects must be serializable.
 * 
 * @author jrd
 */
@SessionScoped
@Named("sessionProxy")
public class SessionProxy implements Serializable, HttpSessionBindingListener {
    
    private final Logger log = LoggerFactory.getLogger(SessionProxy.class);
    /** session attribute name (used for session.setAttribute() / session.removeAttribute() */
   
  
    private ClientEventStream eventStreamQueue;
    private Double latitude, longitude, range;
    private String units = "mi";
    
    public SessionProxy() { 
        eventStreamQueue = new ClientEventStream(ClientEventStream.StreamType.EventSequence, 1000);
        eventStreamQueue.test();
    }

    public ClientEventStream getEventStreamQueue() {
        return eventStreamQueue;
    }

    public void setNewSearchRadius(Double newLat, Double newLong, Double radius, String rUnits) {
        latitude=newLat; longitude=newLong; range=radius; units=rUnits;
    }
    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getRange() {
        return range;
    }

    public String getUnits() {
        return units;
    }
    
    
    
    @Override
    public void valueBound(HttpSessionBindingEvent event) {
       log.info(" added to web session. ");
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        eventStreamQueue.purgeAll();
        log.info(" removed from web session ; passivating. ");
    }
    
}
