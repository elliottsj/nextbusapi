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
package com.elliottsj.nextbus.domain;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *     A group of prediction directions and enclosed predictions for a specified route and stop.
 * </p>
 *
 * Predictions about a Stop are complex; they involve several directions of travel, and multiple vehicles each with forecast arrival times.
 * This domain object is essentially a nested tree of objects. Implementing it as formal Java POJOs makes it much simpler to navigate
 * by the target application programmer who will likely use it to either drive UI code, or feed a JMS event stream.
 *
 * @author jrd
 * @author elliottsj
 */
public class PredictionGroup extends NextbusValueObject implements Comparable<PredictionGroup> {

    private static final long serialVersionUID = -4727046836790039908L;

    /**
     * Full constructor.
     *
     * @param route the route for these predictions
     * @param stop the stop for these predictions
     * @param directions the directions containing the predictions
     * @param messages status information relevant to these predictions
     * @param copyright provided by NextBus
     * @param timestamp epoch milliseconds when this prediction group was created
     */
    public PredictionGroup(Route route, Stop stop, List<PredictionDirection> directions, List<String> messages, String copyright, Long timestamp) {
        super(copyright, timestamp);
        this.directions = directions;
        this.route = route;
        this.stop = stop;
        this.messages = messages;
    }

    /**
     * Domain factory constructor.
     */
    public PredictionGroup(Route route, Stop stop, List<PredictionDirection> directions, List<String> messages, String copyright) {
        this(route, stop, directions, messages, copyright, null);
    }
    
    /**
     * The route for which this sequence of predictions applies
     */
    protected Route route;

    /**
     * The stop for which the sequence of predictions apply.
     */
    protected Stop stop;

    /**
     * The scheduled directions currently available from this stop
     */
    protected List<PredictionDirection> directions;

    /**
     * Any transit agency messages applicable to this prediction (i.e. lateness, outages, etc)
     */
    protected List<String> messages;

    /**
     * Multiple Vehicles travel in a route direction and each of these vehicles holds an arrival prediction.
     */
    public static class PredictionDirection implements Serializable, Comparable<PredictionDirection> {

        private static final long serialVersionUID = -4022455470758079627L;

        /** Transit agency assigned name of this Travel Direction i.e.Back Bay Station via Copley Square */
        protected String title;

        /** Time Prediction elements for each Vehicle currently set out on this Direction. */
        protected List<Prediction> predictions;

        public PredictionDirection(String title, List<Prediction> predictions) {
            this.title = title;
            this.predictions = predictions;
        }
        
        public List<Prediction> getPredictions() {
            return predictions;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public int compareTo(PredictionDirection o) {
            return this.title.compareTo(o.title);
        }

    }

    /**
     * Gets the directions for this prediction group.
     * 
     * @return the available travel directions for this prediction group
     */
    public List<PredictionDirection> getDirections() {
        return directions;
    }

    /**
     * Gets the count of predictions in this group.
     * 
     * @return the total number of prediction elements in the group (across all directions)
     */
    public int getAvailablePredictions() {
        int totalPredictions = 0;
        for (PredictionDirection d : directions)
            totalPredictions += d.predictions.size();
        return totalPredictions;
    }
    
    /**
     * Gets the stop for this prediction group.
     *
     * @return stop for which these predictions apply
     */
    public Stop getStop() {
        return stop;
    }

    /**
     * Gets the route for this prediction group.
     *
     * @return the route for which these predictions apply
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Gets the messages applicable to this prediction group.
     *
     * @return messages connected to this prediction set
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
        return "PredictionGroup{" + "route="+route+", stop=" + stop + ", createTime=" + super.getTimestamp() + ", directions=" + directions.size() + ", predictions=" + this.getAvailablePredictions() + ", messages=" + messages.size() + '}';
    }

    public int compareTo(PredictionGroup o) {
        return this.stop.compareTo(o.stop);
    }
    
}
