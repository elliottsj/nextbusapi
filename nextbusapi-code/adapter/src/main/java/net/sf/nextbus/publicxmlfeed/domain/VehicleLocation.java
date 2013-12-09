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
import java.util.Date;
/**
 * Time and Position snapshot of a Vehicle on a Route and Direction.
 * <vehicle id="2094" routeTag="34" dirTag="34_1_var0" lat="42.2945228" lon="-71.1194256" secsSinceReport="52" predictable="true" heading="39" speedKmHr="0.0"/>
 * <lastTime time="1337181813388"/>
 * @author jrd
 */
public class VehicleLocation extends NextBusValueObject implements IGeocoded {
    static final long serialVersionUID = 6693216783001986829L;

    /** Vehicle identifier */
    protected Vehicle vehicle;
    /** The Route the vehicle is assigned to. */
    protected Route parent;
    /** The Schedule Direction that the vehicle is currently assigned to. */
    protected String directionId;
    /** Are NextBus Predictions currently available for this vehicle? */
    protected boolean predictable;
    /** Position at last time where last time is the NextbusValueObject::createTimeUtc */
    protected Geolocation location;
    /** Vehicle speed in km/hr */
    protected double speed;
    /** Vehicle head in Degrees from Magnetic North */
    protected double heading;

    /**
     * Serialization ctor
     */
    protected VehicleLocation() { }
    
    /**
     * Domain factory ctor.
     */
    public VehicleLocation(Route route, String _vehId, String _dirId, boolean _predictable, Geolocation position, long _lastTime, double _speed, double _hdng, String copyRight ) {
        super(_lastTime, copyRight);
        this.vehicle = new Vehicle(_vehId);
        this.parent= route;
        this.directionId = _dirId;
        this.predictable = _predictable;
        this.speed = _speed;
        this.heading = _hdng;
        this.location=position;
    }
    
    /**
     * 
     * @return Vehicle's ID on a given route. Note this is not unique in certain routes.
     */
     public Vehicle getVehicle() {
        return vehicle;
    }
     
    /**
     * 
     * @return The Direction ID.  Allows for linkage to a Direction object using RouteConfiguration.
     */
    public String getDirectionId() {
        return directionId;
    }

    /**
     * 
     * @return Heading in degrees (0-360) from Compass North. Negative valued if heading is not avail. 
     */
    public double getHeading() {
        return heading;
    }
    /**
     * 
     * @return true if getHeading() will provide an accurate heading. 
     */
    public boolean isHeadingAvailable() {
        return heading >= 0.0;
    }

    /**
     * 
     * @return Last reported time at position in milliSeconds since Jan 1, 1970 UTC.
     */
    public long getLastTimeUtc() {
        return super.createTimeUtc;
    }
    /**
     * Last reported time at position since Jan 1, 1970 UTC.
     */
    public Date getLastTime() {
        return new java.sql.Date(super.createTimeUtc);
    }
    public java.sql.Timestamp getFoo() {
        return new java.sql.Timestamp(super.createTimeUtc);
    }

    /**
     * 
     * @return  The GPS position of the vehicle at the last reported time.
     */
    public Geolocation getGeolocation() {
        return location;
    }

    /**
     * 
     * @return true if Prediction service data is available for this vehicle. 
     */
    public boolean isPredictable() {
        return predictable;
    }

    /**
     * 
     * @return The Route identifier for this vehicle.
     */
    public Route getRoute() {
        return parent;
    }

    /**
     * 
     * @return the vehicle speed of the vehicle in km/Hr (metric)
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @return the vehicle speed in miles per hour (imperial)
     */
    public double getSpeedMPH() {
        return speed * Geolocation.km2miles;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VehicleLocation other = (VehicleLocation) obj;
        if (this.vehicle != other.vehicle && (this.vehicle == null || !this.vehicle.equals(other.vehicle))) {
            return false;
        }
        if (this.parent != other.parent && (this.parent == null || !this.parent.equals(other.parent))) {
            return false;
        }
        if ((this.directionId == null) ? (other.directionId != null) : !this.directionId.equals(other.directionId)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.vehicle != null ? this.vehicle.hashCode() : 0);
        hash = 43 * hash + (this.parent != null ? this.parent.hashCode() : 0);
        hash = 43 * hash + (this.directionId != null ? this.directionId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "VehicleLocation{" + "vehicle=" + vehicle + ", parent=" + parent + ", directionId=" + directionId + ", predictable=" + predictable + ", locationAtLastTime=" + location + ", speed=" + speed + ", heading=" + heading + '}';
    }
    
    
}
