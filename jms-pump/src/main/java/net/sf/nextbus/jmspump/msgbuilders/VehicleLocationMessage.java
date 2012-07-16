/*******************************************************************************
 * Copyright (C) 2011,2012 by James R. Doyle
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
package net.sf.nextbus.jmspump.msgbuilders;
import net.sf.nextbus.publicxmlfeed.domain.VehicleLocation;
import javax.jms.Message;
import javax.jms.MapMessage;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.util.*;

/**
 * A Message Enricher to support the VehicleLocation object.
 * @author jrd
 */
public class VehicleLocationMessage {
 
    List<Message> convert(List<VehicleLocation> vls, Session s) throws JMSException {
        List<Message> r = new ArrayList<Message>();
        for (VehicleLocation vl : vls) {
            r.add(convert(vl, s));
        }
        return r;
    }
    
    Message convert(VehicleLocation vl, Session s) throws JMSException {
        ObjectMessage m = s.createObjectMessage();
        m.setJMSType("VehicleLocation");
        m.setStringProperty("agency", vl.getRoute().getAgency().getId());
        m.setStringProperty("route", vl.getRoute().getTag());
        m.setStringProperty("direction", vl.getDirectionId());
        m.setStringProperty("vehicleId", vl.getVehicle().getId());
        m.setLongProperty  ("lastTimeUTC", vl.getLastTimeUtc()); 
        m.setDoubleProperty("latitude", vl.getGeolocation().getLatitude());
        m.setDoubleProperty("longitude", vl.getGeolocation().getLongitude());
        m.setObject(vl);
        
        return m;
    }
    
    VehicleLocation convert(ObjectMessage m) throws JMSException {
        return (VehicleLocation) m.getObject();
    }
    
}
