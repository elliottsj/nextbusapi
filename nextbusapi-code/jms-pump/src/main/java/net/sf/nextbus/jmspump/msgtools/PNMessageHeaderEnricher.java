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
import org.springframework.integration.Message;
import net.sf.nextbus.publicxmlfeed.domain.Prediction;

/**
 * A Message Enricher to support the Prediction class.
 * @author jrd
 */
public class PNMessageHeaderEnricher {

    protected Prediction pn(Message m) {
        return (Prediction) m.getPayload();
    }

    public String getAgencyId(Message m) {
        return pn(m).getRoute().getAgency().getId();
    }

    // the prediction needs the route
    public String getRouteId(Message m) {
         return pn(m).getRoute().getTag();
    }

    public String getRouteName(Message m) {
        return pn(m).getRoute().getTitle();
    }

    public String getStopId(Message m) {
        return pn(m).getStop().getTag();
    }

    public String getStopName(Message m) {
        return pn(m).getStop().getTitle();
    }

    public String getDirectionId(Message m) {
        return pn(m).getDirectionId();
    }

    public String getVehicleId(Message m) {
        return pn(m).getVehicle().getId();
    }

    public String getTripTag(Message m) {
        return pn(m).getTripTag();
    }

    public String getBlock(Message m) {
        return pn(m).getBlock();
    }

    public String getBranch(Message m) {
        return pn(m).getBlock();
    }

    public Long getPredictedArrivalOrDepartureTimeUTCMilliseconds(Message m) {
        return pn(m).getPredictedArrivalOrDepartureTimeUTCMilliseconds();
    }
    public boolean isArrivalTimePrediction(Message m) {
        return pn(m).isArrivalTimePrediction();
    }

    public boolean isDepartureTimePrediction(Message m) {
        return pn(m).isDepartureTimePrediction();
    }

    public boolean isScheduleBasedPrediction(Message m) {
        return pn(m).isScheduleBasedPrediction();
    }

    public boolean isHeuristicBasedPrediction(Message m) {
        return pn(m).isHeuristicBasedPrediction();
    }
    public boolean isPredictionIncludesLayoverEstimate(Message m) {
        return pn(m).isPredictionIncludesLayoverEstimate();
    }
    public boolean isDelayed(Message m) {
        return pn(m).isDelayed();
    }
    public String getCopyrightNotice(Message m) {
        return pn(m).getCopyrightNotice();
    }
    public long getTimestamp(Message m){
        return pn(m).getObjectTimestamp();
    }
}