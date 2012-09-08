/**
 * *****************************************************************************
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
package net.sf.nextbus.jmspump.msgtools;

import net.sf.nextbus.publicxmlfeed.domain.VehicleLocation;
import org.springframework.integration.Message;

/**
 * A Message Enricher to support the VehicleLocation object. Despite attemps
 * to get SPeL Expression based enrichment to work, it seemed to be buggy and
 * would only work for a single enricher expression. The alternate mechanism,
 * using this bean, seems to be OK in Spring Int 2.1.2
 *
 * @author jrd
 */
public class VLMessageHeaderEnricher {

    protected VehicleLocation vl(Message m) {
        return (VehicleLocation) m.getPayload();
    }
    public String getAgencyId(Message m) {
        return vl(m).getRoute().getAgency().getId();
    }
    public String getRouteId(Message m) {
        return vl(m).getRoute().getTag();
    }
     public String getDirectionId(Message m) {
        return vl(m).getDirectionId();
    }
    public String getVehicleId(Message m) {
        return vl(m).getVehicle().getId();
    }
    public Double getLatitude(Message m) {
        return vl(m).getGeolocation().getLatitude();
    }
    public Double getLongitude(Message m) {
        return vl(m).getGeolocation().getLongitude();
    }
    
    public Long getTimestamp(Message m) {
        return vl(m).getObjectTimestamp();
    }
    public Long getLastReportedTimeAtPosition(Message m) {
        return vl(m).getLastTimeUtc();
    }
    public Long getTimeSkew(Message m) {
        return getTimestamp(m) - getLastReportedTimeAtPosition(m);
    }
    public Boolean isPredictable(Message m) {
        return vl(m).isPredictable();
    }
    public Double getHeading(Message m) {
        return vl(m).getHeading();
    }
    public Double getSpeedKmHr(Message m) {
        return vl(m).getSpeed();
    }
    public Double getSpeedMPH(Message m) {
        return vl(m).getSpeedMPH();
    }
    public Boolean isHeadingAvailable(Message m) {
        return vl(m).isHeadingAvailable();
       
    }
    public String getCopyrightNotice(Message m) {
        return vl(m).getCopyrightNotice();
    }
}
