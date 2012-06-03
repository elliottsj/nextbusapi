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
 * A Direction is a series of Stops on a given Route. This class appears in routeConfig XML
 * (see below) as well as Prediction and Schedule response xml.
 * <pre>
 * <direction tag="34_1_var1" title="Townsend &amp; Humboldt" name="Inbound" useForUI="true">
 *  <stop tag="61871" >
 *  <stop tag="71618" >
 * ...
 * </pre>
 * @author jrd
 */
public class Direction extends NextbusValueObject implements Comparable<Direction> {
    
    static final long serialVersionUID = -3331323894980430611L;
    
    /** The Route that contains (owns) this Direction. */
    protected Route route;
    /** Nextbus assigned unique ID for this Direction example 34_1_var1 */
    protected String tag;
    /** The bus direction schedule title example Townsend & Humboldt */
    protected String title;
    /** The name of the direction example Inbound */
    protected String name;
    /** Stops scheduled on this Direction - but no schedule time information ; use the Schedule API for that data.*/
    protected List<Stop> stops;
    
    /**
     * serialization ctor.
     */
    protected Direction() { }
    
    /**
     * Domain factory ctor.
     */
    public Direction(Route parent, String _tag, String _title, String _name, List<Stop> _stops, String copyright) {
        super(copyright);
        this.route = parent;
        this.tag = _tag; this.name = _name; this.title = _title;
        this.stops = _stops;
    }

    /**
     * 
     * @return Name for this direction (example Inbound, Outbound, etc)
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return The Route that contains this Direction.
     */
    public Route getRoute() {
        return route;
    }

    /**
     * 
     * @return The sequence of Stops (i.e stations) on this Direction.
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     * 
     * @return The identifier element for this Direction
     */
    public String getTag() {
        return tag;
    }

    /**
     * 
     * @return The title of this direction, often found on Schedules.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param obj
     * @return  The Composite of Route and Tag provide the unique identity of objects of this class.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Direction other = (Direction) obj;
        if ((this.tag == null) ? (other.tag != null) : !this.tag.equals(other.tag)) {
            return false;
        }
        if (this.route != other.route && (this.route == null || !this.route.equals(other.route))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.tag != null ? this.tag.hashCode() : 0);
        hash = 59 * hash + (this.route != null ? this.route.hashCode() : 0);
        return hash;
    }

    public int compareTo(Direction o) {
        return o.name.compareTo(o.name);
    }
    
    
}
