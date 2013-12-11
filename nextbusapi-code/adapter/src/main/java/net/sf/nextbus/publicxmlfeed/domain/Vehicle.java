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

/**
 * Simple type for Vehicle ; Note that Vehicles can arbitrarily be assigned across Routes at any time by the Transit agency.
 * The identity of a Vehicle only serves to disambiguate for other vehicles concurrently deployed on the same Route and Direction.
 * 
 * @author jrd
 */
public class Vehicle extends NextBusValueObject implements IGeocoded {

    /** Identifier of this vehicle. It is often but not always numeric. */
    @Attribute
    private final String id;

    /** The route this vehicle is currently associated with. */
    private final Route route;

    /** The direction that this vehicle is currently on. */
    private final Direction direction;

    /** The location of this vehicle. */
    protected Geolocation location;

    @Attribute
    private int secsSinceReport;

    /** Are NextBus Predictions currently available for this vehicle? */
    @Attribute
    protected boolean predictable;

    /** Vehicle head in Degrees from Magnetic North */
    @Attribute
    protected double heading;

    /** Vehicle speed in km/hr */
    @Attribute
    protected double speedKmHr;

    public Vehicle(@Attribute String id,
                   @Attribute String routeTag,
                   @Attribute String dirTag,
                   @Attribute int secsSinceReport,
                   @Attribute boolean predictable,
                   @Attribute double heading,
                   @Attribute double speedKmHr) {
        this.id = id;
        this.route = new Route(routeTag);
        this.direction = new Direction(dirTag);
        this.secsSinceReport = secsSinceReport;
        this.predictable = predictable;
        this.heading = heading;
        this.speedKmHr = speedKmHr;
    }

    public String getId() {
        return id;
    }

    /**
     * Returns the GPS location of a Geocodeable object.
     *
     * @return
     */
    public Geolocation getGeolocation() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vehicle other = (Vehicle) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Vehicle{" + "id=" + id + '}';
    }

}
