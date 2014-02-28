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
 * A stop has a specific name and GPS location; keep in mind that a stop can
 * serve multiple routes
 *
 * @author jrd
 * @author elliottsj
 */
@SuppressWarnings("UnusedDeclaration")
public class Stop extends NextbusValueObject implements IGeocoded, Comparable<Stop> {

    private static final long serialVersionUID = 4758606058242302310L;

    /**
     * Agency owning this stop
     */
    protected Agency agency;

    /**
     * The Key Identifier for this Id - example, 10642
     */
    protected String tag;

    /**
     * Full title of the stop - example, Forest Hills Station Upper Busway
     */
    protected String title;

    /**
     * The shortened title, if available.
     */
    protected String shortTitle;

    /**
     * GPS location of the stop
     */
    protected Geolocation geolocation;

    /**
     * An alternate stop ID published in schedules and used in telephone voice responder and SMS status messages.
     */
    protected String stopId;

    /**
     * Full constructor
     *
     * @param agency the agency owning this stop
     * @param tag the tag of this stop
     * @param title the title of this stop
     * @param shortTitle the short title of this stop
     * @param stopId the id of this stop
     * @param geolocation the geolocation of this stop
     * @param copyright the copyright text from NextBus
     * @param timestamp epoch milliseconds when this stop was created
     */
    public Stop(Agency agency, String tag, String title, String shortTitle, String stopId, Geolocation geolocation, String copyright, Long timestamp) {
        super(copyright, timestamp);
        this.agency = agency;
        this.tag = tag;
        this.title = title;
        this.shortTitle = shortTitle;
        this.geolocation = geolocation;
        this.stopId = stopId;
    }

    /**
     * Domain factory constructor.
     */
    public Stop(Agency agency, String tag, String title, String shortTitle, String stopId, Geolocation geolocation, String copyright) {
        this(agency, tag, title, shortTitle, stopId, geolocation, copyright, null);
    }

    /**
     * Gets the location of this stop.
     *
     * @return the GPS location of the stop
     */
    public Geolocation getGeolocation() {
        return geolocation;
    }

    /**
     * Gets the agency for this stop.
     *
     * @return the agency for this stop
     */
    public Agency getAgency() {
        return agency;
    }

    /**
     * Gets the short title for this stop.
     *
     * @return the shortened title for mobile devices, if available from NextBus
     */
    public String getShortTitle() {
        return shortTitle;
    }

    /**
     * Gets the ID of this stop.
     *
     * @return the optional stopId (not the same as the tag identifier!) that is used for telephone or SMS
     */
    public String getStopId() {
        return stopId;
    }

    /**
     * Gets the unique tag for this stop.
     *
     * @return the identifier key for this stop
     */
    public String getTag() {
        return tag;
    }

    /**
     * Gets the title of this stop.
     *
     * @return The human readable title or location name of this stop
     */
    public String getTitle() {
        return title;
    }

    /**
     * Utility finder to locate a stop by its tag.
     *
     * @param stops a list of stops
     * @param tag the stop tag to find
     * @return the stop with the given tag
     * @throws IllegalArgumentException if the stop cant be found
     */
    public static Stop find(List<Stop> stops, String tag) {
        if (stops == null || tag == null || stops.isEmpty())
            throw new IllegalArgumentException("Illegal arguments, List<Stop> is null or empty, or tag is null or empty.");
        for (Stop s : stops)
            if (s.getTag().equals(tag))
                return s;
        throw new IllegalArgumentException("No Stop object for tag=" + tag + " in List<Stop> given");
    }

    /**
     * The Composite of Route and tag define the unique identity for objects of
     * this class.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stop)) return false;

        Stop stop = (Stop) o;

        return agency.equals(stop.agency) && tag.equals(stop.tag);
    }

    @Override
    public int hashCode() {
        int result = agency.hashCode();
        result = 31 * result + tag.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Stop{" + "tag=" + tag + ", title=" + title + ", shortTitle=" + shortTitle + ", agency=" + agency + ", geolocation=" + geolocation + ", stopId=" + stopId + '}';
    }

    public int compareTo(Stop o) {
        return this.title.compareTo(o.title);
    }


}
