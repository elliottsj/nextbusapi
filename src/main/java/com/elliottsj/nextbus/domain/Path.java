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
import java.util.List;

/**
 * A Path is an illustrative chain of GPS points that roughly indicates the travelling route of a Vehicle on a Route.
 * This object is provided by the RouteConfiguration with the intention of allowing a UI developer to plot the 
 * points on a map.
 * @author jrd
 */
public class Path extends NextbusValueObject {

    private static final long serialVersionUID = -3827290646579333117L;

    /** The Route containing this Path. */
    protected Route route;
    /** The Path id, assigned by Nextbus, but not necessarily human readable. */
    protected String pathId;
    /** Sequence of GPS locations illustrating the path of travel. */
    protected List<Geolocation> points;

    /**
     * Full constructor
     *
     * @param parent route owning this path
     * @param pathId id of this path
     * @param geolocations points on this path
     * @param copyright text provided by NextBus
     * @param timestamp epoch milliseconds when this path was created
     */
    public Path(Route parent, String pathId, List<Geolocation> geolocations, String copyright, Long timestamp) {
        super(copyright, timestamp);
        this.route = parent;
        this.pathId = pathId;
        this.points = geolocations;
    }

    /**
     * Domain factory constructor
     */
    public Path(Route parent, String pathId, List<Geolocation> geolocations, String copyright) {
        this(parent, pathId, geolocations, copyright, null);
    }

    public Route getRoute() {
        return route;
    }

    /**
     * Because the NextBus Web Service API fails to uniquely identify a <path></path>
     * grouping, we create a synthetic identifier for them on the fly by means of an
     * incrementing integer.  We use a String rather than an int in the event that they
     * should add this ID attribute to their wire protocol in the future. <jrd>
     * @return the path Id
     */
    public String getPathId() {
        return pathId;
    }
    
    /**
     * 
     * @return The sequences of GPS points that illustrates the travel route on a map.
     */
    public List<Geolocation> getPoints() {
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Path)) return false;

        Path path = (Path) o;

        return pathId.equals(path.pathId) && route.equals(path.route);
    }

    @Override
    public int hashCode() {
        int result = route.hashCode();
        result = 31 * result + pathId.hashCode();
        return result;
    }

}
