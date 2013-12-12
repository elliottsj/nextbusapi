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
    private String id;

    /** The route this vehicle is currently associated with. */
    private Route route;

    /** The direction that this vehicle is currently on. */
    private Direction direction;

    /** The location of this vehicle. */
    private GeolocationTemporal location;

    @Attribute
    private int secsSinceReport;

    /** Are NextBus Predictions currently available for this vehicle? */
    @Attribute
    private boolean predictable;

    /** Vehicle head in Degrees from Magnetic North */
    @Attribute
    private double heading;

    /** Vehicle speed in km/hr */
    @Attribute
    private double speedKmHr;

    public Vehicle(@Attribute String id,
                   @Attribute String routeTag,
                   @Attribute String dirTag,
                   @Attribute double lat,
                   @Attribute double lon,
                   @Attribute int secsSinceReport,
                   @Attribute boolean predictable,
                   @Attribute double heading,
                   @Attribute double speedKmHr) {
        this.id = id;
        this.route = new Route(routeTag);
        this.direction = new Direction(dirTag);
        this.location = new GeolocationTemporal(lat, lon, secsSinceReport);
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
    public GeolocationTemporal getGeolocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;

        Vehicle vehicle = (Vehicle) o;

        return id.equals(vehicle.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Vehicle{" + "id=" + id + '}';
    }

}
