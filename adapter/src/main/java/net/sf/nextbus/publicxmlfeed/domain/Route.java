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
 * A route operated by a transit agency served by NextBus.
 *
 * @author jrd
 * @author elliottsj
 */
@SuppressWarnings("UnusedDeclaration")
public class Route extends NextbusValueObject {

    private static final long serialVersionUID = 3327736979892313874L;

    /**
     * Transit agency that runs this route.
     */
    protected Agency agency;

    /**
     * Route identifier
     */
    protected String tag;

    /**
     * Full schedule title of the route (optional)
     */
    protected String title;

    /**
     * Shortened or abbreviated route name (optional)
     */
    protected String shortTitle;

    /**
     * Full constructor
     *
     * @param agency the agency owning this route
     * @param tag the tag of this route
     * @param title the title of this route
     * @param shortTitle the short title of this route
     * @param copyright the copyright text provided by NextBus
     * @param timestamp epoch milliseconds when this route was created
     */
    public Route(Agency agency, String tag, String title, String shortTitle, String copyright, Long timestamp) {
        super(copyright, timestamp);
        this.agency = agency;
        this.tag = tag;
        this.title = title;
        this.shortTitle = shortTitle;
    }

    /**
     * Domain factory constructor
     */
    public Route(Agency agency, String tag, String title, String shortTitle, String copyrightText) {
        this(agency, tag, title, shortTitle, copyrightText, null);
    }

    /**
     * Gets the agency that owns this route.
     *
     * @return the agency that owns this route
     */
    public Agency getAgency() {
        return agency;
    }

    /**
     * Gets the unique tag of this route.
     *
     * @return the Identifier tag of this route.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Gets the title of this route.
     *
     * @return the human-readable title of this route
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the short title of this route.
     *
     * @return the shortened title of this route, for small device displays
     */
    public String getShortTitle() {
        return shortTitle;
    }

    /**
     * Route identity is composite of the agency and tag.
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

    /**
     * Utility finder to locate a route by its tag.
     *
     * @param routes a list of routes
     * @param tag the tag of the route to find
     * @return the route with the given tag
     * @exception IllegalArgumentException if the route cannot be found
     */
    public static Route find(List<Route> routes, String tag) {
        if (routes == null || tag == null || routes.isEmpty())
            throw new IllegalArgumentException("Illegal arguments; List<Route> is null or empty, or tag is null or empty.");
        for (Route route : routes)
            if (route.getTag().equals(tag)) return route;
        throw new IllegalArgumentException("No Route object for tag=" + tag + " in List<Route> given");
    }

    @Override
    public String toString() {
        return "Route{" + "agency=" + agency + ", tag=" + tag + ", title=" + title + '}';
    }

}
