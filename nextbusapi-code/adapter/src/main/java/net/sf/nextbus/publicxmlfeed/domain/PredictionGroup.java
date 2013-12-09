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

import java.util.List;

/**
 * Predictions about a Stop are complex ; they involve several Directions of travel, and Multiple vehicles each with forecast arrival times.
 * This domain object is essentially a nested tree of objects. Implementing it as formal Java POJOs makes it much simpler to navigate
 * by the target application programmer who will likely use it to either drive UI code, or feed a JMS event stream.
 * <bold>Seriously needs a UML Diagram</bold>
 * 
 * <pre>
 * <predictions agencyTitle="San Francisco Muni, CA" routeTag="N" routeCode="1" routeTitle="N - Judah" stopTitle="Civic Center Station Outbound">
 * <prediction>...</prediction>
 * ...
 * <prediction>...</prediction>
 * <message>..</message>
 * ..
 * <message>..</message>
 * </prediction>
 * </pre> @author jrd
 */
public class PredictionGroup extends NextBusValueObject implements Comparable<PredictionGroup> {
    static final long serialVersionUID = 4961855382838833913L;
    /**
     * serialization ctor.
     */
    protected PredictionGroup() { }

    /**
     * Domain factory ctor.
     */
    public PredictionGroup(Route _rte, Stop _stop, List<PredictionDirection> drns, String copyright, List<String> msgs) {
        super(copyright);
        this.directions = drns;
        this.route = _rte;
        this.stop = _stop;
        this.messages = msgs;
    }
    
    /** The Route for which this sequence of Predictions applies */
    protected Route route;
    /** The Stop for which the sequence of Predictions apply. */
    protected Stop stop;
    /** The scheduled Directions currently available from this Stop */
    protected List<PredictionDirection> directions;
    /** Any Transit agency messages applicable to this prediction (i.e. lateness, outages, etc)  */
    protected List<String> messages;

    /**
     * Multiple Vehicles travel in a route Direction and each of these vehicles holds an arrival prediction.
     */
    public static class PredictionDirection implements Comparable<PredictionDirection>, java.io.Serializable {
        static final long serialVersionUID = -1405165652013067022L;
        
        public PredictionDirection(String _title, List<Prediction> pdns) {
            this.title=_title;
            this.predictions=pdns;
        }
        protected PredictionDirection() { }
        /** Transit agency assigned name of this Travel Direction i.e.Back Bay Station via Copley Square */
        protected String title;
        /** Time Prediction elements for each Vehicle currently set out on this Direction. */
        protected List<Prediction> predictions;
        
        public List<Prediction> getPredictions() {
            return predictions;
        }

        public String getTitle() {
            return title;
        }

        public int compareTo(PredictionDirection o) {
          return this.title.compareTo(o.title);
        }
    }

    /**
     * 
     * @return The available Travel directions for this prediction group.
     */
    public List<PredictionDirection> getDirections() {
        return directions;
    }

    /**
     * 
     * @return The total number of Prediction elements in the group (across all Directions)
     */
    public int getAvailablePredictions() {
        int totalPredictions = 0;
            for (PredictionDirection d : directions) {
                totalPredictions += d.predictions.size();
            }
        return totalPredictions;
    }
    
    /**
     *
     * @return Stop which these predictions apply.
     */
    public Stop getStop() {
        return stop;
    }

    public Route getRoute() {
        return route;
    }

    /**
     *
     * @return Messages connected to this Prediction set.
     */
    public List<String> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PredictionGroup other = (PredictionGroup) obj;
        if (this.route != other.route && (this.route == null || !this.route.equals(other.route))) {
            return false;
        }
        if (this.stop != other.stop && (this.stop == null || !this.stop.equals(other.stop))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.route != null ? this.route.hashCode() : 0);
        hash = 67 * hash + (this.stop != null ? this.stop.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        
        return "PredictionGroup{" + "route="+route+", stop=" + stop + ", createTime=" + super.getObjectTimestamp() + ", directions=" + directions.size() + ", predictions=" + this.getAvailablePredictions() + ", messages=" + messages.size() + '}';
    }

    public int compareTo(PredictionGroup o) {
        return this.stop.compareTo(o.stop);
    }
    
    
}
