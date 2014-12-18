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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
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
@DatabaseTable(tableName = "directions")
public class Direction extends NextbusValueObject implements Comparable<Direction> {

    private static final long serialVersionUID = 3520874534321132581L;

    public static final String FIELD_ROUTE_ID = "route_id";
    public static final String FIELD_TAG = "tag";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_NAME = "name";

    /** The Route that contains (owns) this Direction. */
    @DatabaseField(columnName = FIELD_ROUTE_ID, canBeNull = false, foreign = true)
    protected Route route;

    /** Nextbus assigned unique ID for this Direction example 34_1_var1 */
    @DatabaseField(columnName = FIELD_TAG, canBeNull = false)
    protected String tag;

    /** The bus direction schedule title example Townsend & Humboldt */
    @DatabaseField(columnName = FIELD_TITLE, canBeNull = false)
    protected String title;

    /** The name of the direction example Inbound */
    @DatabaseField(columnName = FIELD_NAME, canBeNull = false)
    protected String name;

    /** Stops scheduled on this Direction - but no schedule time information ; use the Schedule API for that data.*/
    @ForeignCollectionField(foreignFieldName = "direction")
    protected Collection<DirectionStop> stops;

    /**
     * Empty constructor for OrmLite
     */
    Direction() {
    }

    /**
     * Full constructor
     *
     * @param parent route that owns this direction
     * @param tag tag of this direction
     * @param title title of this direction
     * @param name name of this direction
     * @param stops stops belonging to this direction
     * @param copyright copyright text provided by NextBus
     * @param timestamp epoch milliseconds when this direction was created
     */
    public Direction(Route parent, String tag, String title, String name, List<Stop> stops, String copyright, Long timestamp) {
        super(copyright, timestamp);
        this.route = parent;
        this.tag = tag;
        this.name = name;
        this.title = title;

        this.stops = new ArrayList<DirectionStop>();
        for (Stop stop : stops)
            this.stops.add(new DirectionStop(this, stop));
    }

    /**
     * Domain factory constructor.
     */
    public Direction(Route parent, String tag, String title, String name, List<Stop> stops, String copyright) {
        this(parent, tag, title, name, stops, copyright, null);
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

    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     *
     * @return The sequence of Stops (i.e stations) on this Direction.
     */
    public List<Stop> getStops() {
        List<Stop> stops = new ArrayList<Stop>();
        for (DirectionStop stop : this.stops)
            stops.add(stop.getStop());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Direction)) return false;

        Direction direction = (Direction) o;

        if (route != null ? !route.equals(direction.route) : direction.route != null) return false;
        if (tag != null ? !tag.equals(direction.tag) : direction.tag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = route != null ? route.hashCode() : 0;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "Direction{title=" + title + '}';
    }


    public int compareTo(Direction o) {
        return o.name.compareTo(o.name);
    }

}
