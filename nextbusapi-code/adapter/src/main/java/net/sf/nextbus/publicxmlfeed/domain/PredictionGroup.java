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
package net.sf.nextbus.publicxmlfeed.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Predictions about a Stop are complex; they involve several Directions of travel, and Multiple vehicles each with forecast arrival times.
 * This domain object is essentially a nested tree of objects. Implementing it as formal Java POJOs makes it much simpler to navigate
 * by the target application programmer who will likely use it to either drive UI code, or feed a JMS event stream.
 */
public class PredictionGroup extends NextBusValueObject {

    private static final long serialVersionUID = 4359081328856210694L;

    /** The Route for which this sequence of Predictions applies */
    private Route route;

    /** The Stop for which this sequence of Predictions applies. */
    private Stop stop;

    /** The scheduled Directions currently available from this Stop */
    @ElementList(inline = true, name = "direction")
    private List<PredictionDirection> directions;

    /** Any Transit agency messages applicable to this prediction (i.e. lateness, outages, etc)  */
    @ElementList(inline = true, name = "message")
    private List<String> messages;

    public PredictionGroup(@Attribute String routeTitle,
                           @Attribute String routeTag,
                           @Attribute String stopTitle,
                           @Attribute String stopTag) {
        this.route = new Route(routeTag, routeTitle);
        this.stop = new Stop(stopTag, stopTitle);
    }

    /**
     * @return the available travel directions for this prediction group
     */
    public List<PredictionDirection> getDirections() {
        return directions;
    }

    /**
     * @return the total number of Prediction elements in the group (across all Directions)
     */
    public int getAvailablePredictions() {
        int totalPredictions = 0;
        for (PredictionDirection d : directions)
            totalPredictions += d.predictions.size();
        return totalPredictions;
    }
    
    /**
     * @return Stop for which these predictions apply
     */
    public Stop getStop() {
        return stop;
    }

    /**
     * @return Route for which these predictions apply
     */
    public Route getRoute() {
        return route;
    }

    /**
     * @return messages connected to this Prediction set
     */
    public List<String> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PredictionGroup)) return false;

        PredictionGroup that = (PredictionGroup) o;

        return route.equals(that.route) && stop.equals(that.stop);
    }

    @Override
    public int hashCode() {
        int result = route.hashCode();
        result = 31 * result + stop.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PredictionGroup{" + "route="+route+", stop=" + stop + ", createTime=" + super.getObjectTimestamp() + ", directions=" + directions.size() + ", predictions=" + this.getAvailablePredictions() + ", messages=" + messages.size() + '}';
    }

    /**
     * Multiple Vehicles travel in a route Direction and each of these vehicles holds an arrival prediction.
     */
    public static class PredictionDirection extends NextBusValueObject {

        private static final long serialVersionUID = -3769931284954942239L;

        /** Transit agency assigned name of this Travel Direction e.g. Back Bay Station via Copley Square */
        @Attribute
        private String title;

        /** Time Prediction elements for each Vehicle currently set out on this Direction. */
        @ElementList(inline = true)
        private List<Prediction> predictions;

        public String getTitle() {
            return title;
        }

        public List<Prediction> getPredictions() {
            return predictions;
        }

    }

}
