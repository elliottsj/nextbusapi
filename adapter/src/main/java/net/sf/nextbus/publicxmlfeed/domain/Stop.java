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
 * A stop has a specific name and GPS location ; Keep in mind that a Stop can
 * serve multiple Routes
 *
 * <stop tag="10642" title="Forest Hills Station Upper Busway" lat="42.3005199"
 * lon="-71.11369" stopId="10642"/>
 * </pre>
 *
 * @author jrd
 */
public class Stop extends NextbusValueObject implements IGeocoded, Comparable<Stop> {

    /** Agency owning this stop*/
    protected Agency agency;
    /** The Key Identifier for this Id - example, 10642*/
    protected String tag;
    /** Full title of the stop - example, Forest Hills Station Upper Busway */
    protected String title;
    /** The shortened title, if available. */
    protected String shortTitle;
    /** GPS location of the stop */
    protected Geolocation geolocation;
    /** An Alternate StopID published in Schedules and used in Telephone Voiceresponder and SMS Status messages. */
    protected String stopId;

    /**
     * Full constructor
     *
     * @param agency the agency owning this stop
     * @param id the id of this stop
     * @param tag the tag of this stop
     * @param title the title of this stop
     * @param shortTitle the short title of this stop
     * @param geolocation the geolocation of this stop
     * @param copyrightText the copyright text from NextBus
     * @param timestamp epoch milliseconds when this stop was created
     */
    public Stop(Agency agency, String id, String tag, String title, String shortTitle, Geolocation geolocation, String copyrightText, long timestamp) {
        super(timestamp, copyrightText);
        this.shortTitle = shortTitle;
        this.agency = agency;
        this.stopId = id;
        this.tag = tag;
        this.title = title;
        this.geolocation = geolocation;
    }

    /**
     * Domain factory ctor.
     */
    public Stop(Agency agency, String id, String tag, String title, String shortTitle, Geolocation geolocation, String copyrightText) {
        super(copyrightText);
        this.shortTitle = shortTitle;
        this.agency = agency;
        stopId = id;
        this.tag = tag;
        this.title = title;
        this.geolocation = geolocation;
    }

    /**
     *
     * @return The GPS location of the Stop or Station.
     */
    public Geolocation getGeolocation() {
        return geolocation;
    }

    /**
     *
     * @return The parent Route that this Stop is bound on. Note that a
     * particular Physical stop may sit on multiple routes.
     */
    public Agency getAgency() {
        return agency;
    }

    /**
     * @return The shortened title for mobile devices, if available from
     * Nextbus.
     */
    public String getShortTitle() {
        return shortTitle;
    }

    /**
     * @return The optional StopID (not the same as the tag identifier!) that is
     * used to Telephone or SMS ID of this Stop.
     */
    public String getStopId() {
        return stopId;
    }

    /**
     * @return The identifier key for this stop along a station.
     */
    public String getTag() {
        return tag;
    }

    /**
     *
     * @return The human readable title or location name of this Stop.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Utility finder to locate a Stop by friendly String is
     * @param stops List of stops
     * @param id the stop ID to find
     * @return the Stop
     * @exception IllegalArgumentException if the stop cant be found.
     */
    public static Stop find(List<Stop> stops, String id) {
        if (stops==null || id==null || stops.isEmpty()) {
            throw new IllegalArgumentException("Illegal arguments, List<Stop> is null or empty, or id is null or empty.");
        }
        for (Stop s : stops) {
            if (s.getTag().equals(id)) return s;
        }
        throw new IllegalArgumentException("No Stop object for id="+id+" in List<Stop> given");
    }
    
    /**
     * The Composite of Route and tag define the unique identity for objects of
     * this class.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Stop other = (Stop) obj;
        if ((this.tag == null) ? (other.tag != null) : !this.tag.equals(other.tag)) {
            return false;
        }
        if (this.agency != other.agency && (this.agency == null || !this.agency.equals(other.agency))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.tag != null ? this.tag.hashCode() : 0);
        hash = 17 * hash + (this.agency != null ? this.agency.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Stop{" + "tag=" + tag + ", title=" + title + ", shortTitle=" + shortTitle + ", agency=" + agency + ", geolocation=" + geolocation + ", stopId=" + stopId + '}';
    }

    public int compareTo(Stop o) {
        return this.title.compareTo(o.title);
    }
    
    
}
