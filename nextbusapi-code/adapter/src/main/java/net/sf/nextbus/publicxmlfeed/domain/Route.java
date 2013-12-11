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
import org.simpleframework.xml.Root;

/**
 * Routes which may enclose Directions, Stops, have assigned Vehicles and also have posted Schedules.
 * Stops can be either shared, or disjoint to other Routes.
 */
@Root
public class Route extends NextBusValueObject {

    private static final long serialVersionUID = -2573832634856836150L;

    /** Transit agency that runs this route. */
    private Agency agency;

    /** Route identifier */
    @Attribute
    private String tag;

    /** Full schedule title of the Route (optional) */
    @Attribute(required = false)
    private String title;

    /** Shortened or abbreviated Route name (optional) */
    @Attribute(required = false)
    private String shortTitle;

    /**
     * @return the agency that owns this route
     */
    public Agency getAgency() {
        return agency;
    }

    /**
     * @param agency the agency that owns this route
     */
    protected void setAgency(Agency agency) {
        this.agency = agency;
    }

    /**
     * @return the identifier tag of this route
     */
    public String getTag() {
        return tag;
    }

    /**
     * @return the human-readable title of this route
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the shortened title of this route, for small device displays
     */
    public String getShortTitle() {
        return shortTitle;
    }

    /**
     * The identity of this route is a composite on agency and tag.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;

        Route route = (Route) o;

        return agency.equals(route.agency) && tag.equals(route.tag);
    }

    @Override
    public int hashCode() {
        int result = agency.hashCode();
        result = 31 * result + tag.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Route{" + "agency=" + agency + ", tag=" + tag + ", title=" + title + '}';
    }
}
