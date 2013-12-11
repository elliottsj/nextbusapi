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
 * A Path is an illustrative chain of GPS points that roughly indicates the travelling route of a Vehicle on a Route.
 * This object is provided by the RouteConfiguration with the intention of allowing a UI developer to plot the 
 * points on a map.
 * @author jrd
 */
public class Path extends NextBusValueObject {
    
    static final long serialVersionUID = -3827290646579333117L;
    /** The Route containing this Path. */
    protected Route route;
    /** The Path id, assigned by Nextbus, but not necessarily human readable. */
    protected String pathId;
    /** Sequence of GPS locations illustrating the path of travel. */
    protected List<Geolocation> points;
    
    /** ctor */
    public Path(Route parent, String _pathId, List<Geolocation> pts) {
        this.route = parent; this.pathId=_pathId; this.points = pts;
    }

    public Route getRoute() {
        return route;
    }

    /**
     * Because the NextBus Web Service API fails to uniquely identify a <path></path>
     * grouping, we create a synthetic identifier for them on the fly by means of an
     * incrementing integer.  We use a String rather than an int in the event that they
     * should add this ID attribute to their wire protocol in the fututre. <jrd>
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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Path other = (Path) obj;
        if (this.route != other.route && (this.route == null || !this.route.equals(other.route))) {
            return false;
        }
        if ((this.pathId == null) ? (other.pathId != null) : !this.pathId.equals(other.pathId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.route != null ? this.route.hashCode() : 0);
        hash = 73 * hash + (this.pathId != null ? this.pathId.hashCode() : 0);
        return hash;
    }
}
