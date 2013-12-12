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
import net.sf.nextbus.html5demo.service.RadarService;
import javax.ejb.EJB;
import net.sf.nextbus.html5demo.service.EventStreamDemultiplexer;
import java.security.Principal;

/**
 * Web Session context for the Geofeed POC. All web session objects must be
 * serializable.
 *
 * @author jrd
 */
@SessionScoped
@Named("sessionProxy")
public class SessionProxy implements Serializable, HttpSessionBindingListener {
    
    private static final Logger log = LoggerFactory.getLogger(SessionProxy.class);
    /**
     * session attribute name (used for session.setAttribute() /
     * session.removeAttribute()
     */
    @EJB
    private RadarService radar;
    
    private Double latitude, longitude;
    private Double range = 1.0;
    private String units = "mi";
    private ClientEventStream eventStreamQueue;
    private Geoselector streamSelector;
    
    public SessionProxy() {        
    }
    
    public void init(String sessionId, Principal owner) {
        radar.registerWebSessionId(sessionId);
        streamSelector = radar.getSelector();
        eventStreamQueue = radar.getEventStream();
        eventStreamQueue.setOwner(owner);
        eventStreamQueue.test();
    }
    public void close() {
         radar.close();
    }
    public ClientEventStream getEventStreamQueue() {
        return eventStreamQueue;
    }
    
    public void setNewSearchRadius(Double newLat, Double newLong, Double radius, String rUnits) {
        Double radiusKm = rUnits.equalsIgnoreCase("km") ? radius : radius / 2;
        eventStreamQueue.purgeAll();
        streamSelector.setNewSearchRadius(newLat, newLong, radiusKm);
        latitude = newLat;
        longitude = newLong;
        range = radius;
        units = rUnits;
        log.debug("set location to (%f,%f) for %f %s", new Object[]{latitude, longitude, range, rUnits});
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
        log.info(" REMOVED! from web session ; passivating. ");
       
    }
    
}
