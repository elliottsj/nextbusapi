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
 * Routes may have multiple Directions, each which further have Stops that can
 * be either shared, or disjoint, to other Routes.
 *
 * Transit systems have routes which may further enclose Directions, Stops, have
 * assigned Vehicles and also have posted Schedules. This class reflects the
 * meta-data conveyed in the various XML Response Streams of NextBus.
 *
 * @author jrd
 */
public class Route extends NextbusValueObject {

    static final long serialVersionUID = -3258970231092816300L;
    /** Transit agency that runs this route. */
    protected Agency agency;
    /** Route identifier */
    public String tag;
    /** optional - Full schedule title of the Route */
    protected String title;
    /** optional - Shortened or abbreviated Route name */
    protected String shortTitle;

    /**
     * serialization ctor.
     */
    protected Route() {
    }

    /**
     * Domain factory ctor.
     */
    public Route(Agency a, String _tag, String _title, String _shortTitle, String copyrighttext) {
        super(copyrighttext);
        this.title = this.shortTitle = "";
        this.agency = a;
        this.tag = _tag;
        if (_title != null) this.title = _title;
        if (_shortTitle != null) this.shortTitle = _shortTitle;
    }

    /**
     *
     * @return The agency that owns this Route.
     */
    public Agency getAgency() {
        return agency;
    }

    /**
     *
     * @return the Identifier tag of this route.
     */
    public String getTag() {
        return tag;
    }

    /**
     *
     * @return the human readable title of this route.
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return The shortened title of this route, for small device displays
     */
    public String getShortTitle() {
        return shortTitle;
    }

    /**
     * The identity of this object is a Composite on Agency and tag.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Route other = (Route) obj;
        if (this.agency != other.agency && (this.agency == null || !this.agency.equals(other.agency))) {
            return false;
        }
        if ((this.tag == null) ? (other.tag != null) : !this.tag.equals(other.tag)) {
            return false;
        }
        return true;
    }

    /**
     * Utility finder to locate a Route by friendly String is
     * @param routes List of routes
     * @param id the route ID to find
     * @return the Route
     * @exception IllegalArgumentException if the route cant be found.
     */
    public static Route find(List<Route> routes, String id) {
        if (routes==null || id==null || routes.isEmpty()) {
            throw new IllegalArgumentException("Illegal arguments, List<Route> is null or empty, or id is null or empty.");
        }
        for (Route r : routes) {
            if (r.getTag().equals(id)) return r;
        }
        throw new IllegalArgumentException("No Route object for id="+id+" in List<Route> given");
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.agency != null ? this.agency.hashCode() : 0);
        hash = 97 * hash + (this.tag != null ? this.tag.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Route{" + "agency=" + agency + ", tag=" + tag + ", title=" + title + '}';
    }
}
