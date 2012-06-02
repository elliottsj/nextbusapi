/**
 * *****************************************************************************
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
 * Usage of the NextBus Web Service and its data is subject to separate Terms
 * and Conditions of Use (License) available at:
 *
 * http://www.nextbus.com/xmlFeedDocs/NextBusXMLFeed.pdf
 *
 *
 * NextBus® is a registered trademark of Webtech Wireless Inc.
 *
 *****************************************************************************
 */
package net.sf.nextbus.publicxmlfeed.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

/**
 * NextBus uses GPS locations to designate station Stops as well as current
 * Vehicle location. This is a basic Geocode class. There are many other
 * implementation of Geolocation libaries for Android and even other platforms.
 * You can always subclass from these Domain classes to convert to some other
 * Geocode type that you need or prefer.
 *
 * @author jrd
 */
public class Geolocation implements Serializable {

    static final long serialVersionUID = -5569276801266595860L;
    /**
     * Constant: Mean radius of Earth in km [International Union of Geodesy and
     * Geophysics (IUGG])
     */
    public static final double Re = 6371.009; // mean radius of earth in kilometers
    /**
     * Constant: Conversion of kilometers to miles
     */
    public static final double km2miles = 0.621371192;

    /**
     * Haversine distance metric
     *
     * @param p1 Geolocation 1 - with angles in DEGREES
     * @param p2 Geolocation 2 - with angles in DEGREES
     * @return Angular deviation (in RADIANS) on surface of a sphere betw points
     * 1 and 2
     */
    private static double haversine(Geolocation p1, Geolocation p2) {
        // Convert to Radians and compute differentials
        double dθ = p2.latitudeRadians - p1.latitudeRadians;
        double dφ = p2.longitudeRadians - p1.longitudeRadians;
        double Θ1 = p1.latitudeRadians;
        double Θ2 = p2.latitudeRadians;
        // do the the angular distance magic constrained to the surface a sphere
        double a = Math.sin(dθ / 2) * Math.sin(dθ / 2)
                + Math.sin(dφ / 2) * Math.sin(dφ / 2) * Math.cos(Θ1) * Math.cos(Θ2);
        // Convert back to an angle
        double θ = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return θ;
    }

    /**
     * Gets the distance between 2 GPS points in Kilometers.
     *
     * @param p1 Geolocation 1
     * @param p2 Geolocation 2
     * @return absolute distance between points 1 and 2 in km
     */
    public static double distanceKm(Geolocation p1, Geolocation p2) {
        double θ = haversine(p1, p2);
        // compute the arc-length ; s = rθ
        double distance = Re * θ;
        return Math.abs(distance);
    }

    /**
     * Gets the distance between two GPS points in Miles.
     *
     * @param p1
     * @param p2
     * @return absolute distance between points p1 and p2 in miles
     */
    public static double distanceMiles(Geolocation p1, Geolocation p2) {
        double θ = haversine(p1, p2);
        // compute the arc-length ; s = rθ
        double distance = km2miles * Re * θ;
        return Math.abs(distance);
    }
    /**
     * GPS Latitude in Degrees Decimal.
     */
    protected double latitude, latitudeRadians;
    /**
     * GPS Longitude in Degrees Decimal.
     */
    protected double longitude, longitudeRadians;

    /**
     * serialization ctor
     */
    protected Geolocation() {
    }

    /**
     * ctor.
     *
     * @param lat Degrees latitude. Negative values are South, Positive values
     * are North.
     * @param lon Degrees longitude. Negative values are West, Positive values
     * are East.
     */
    public Geolocation(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
        this.latitudeRadians = Math.toRadians(lat);
        this.longitudeRadians = Math.toRadians(lon);
    }

    /**
     * @return Negative values are South, Positive values are North.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @return Degrees longitude. Negative values are West, Positive values are
     * East.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param other
     * @return Distance in km from other point.
     */
    public double getDistanceInKm(Geolocation other) {
        return distanceKm(this, other);
    }

    /**
     * @param other
     * @return Distance in miles from other point.
     */
    public double getDistanceInMiles(Geolocation other) {
        return distanceMiles(this, other);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Geolocation other = (Geolocation) obj;
        if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude)) {
            return false;
        }
        if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(this.latitude) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(this.longitude) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        String lat = "N";
        String lon = "E";

        if (latitude < 0.0) {
            lat = "S";
        }
        if (longitude < 0.0) {
            lon = "W";
        }
        return "Geolocation{" + "latitude=" + latitude + " " + lat + ", longitude=" + longitude + " " + lon + '}';
    }

    /**
     * Utility finder to get the closest IGeocoded object from any given
     * Geolocation.
     *
     * @param c any non-empty collection
     * @param myLocation the GPS coords which you want to minimize against
     * @return The geographically closest object
     */
    public static IGeocoded getClosest(Collection<IGeocoded> items, Geolocation refLocation) {
        if (items == null || items.isEmpty() || refLocation == null) {
            throw new IllegalArgumentException("Empty or null arguments not acceptable.");
        }
        Iterator<IGeocoded> vi = items.iterator();

        // super-simple sort to find the Min(distance) 
        double closestDistance = Double.MAX_VALUE;
        IGeocoded closest = vi.next();
        while (vi.hasNext()) {
            IGeocoded v = vi.next();
            double thisDistance = closest.getGeolocation().distanceKm(v.getGeolocation(), refLocation);
            if (thisDistance < closestDistance) {
                closest = v;
                closestDistance = thisDistance;
            }
        }
        return closest;
    }

    /**
     * A simple proximity sort implemented using the Geocode metric and a Linked
     * List. This relies on Generics, and the fact that two of our Domain types,
     * Stop and VehicleLocation, both confirm to the IGeocoded interface. This
     * sort will cover collections of both types.
     *
     * @param items A Collection of IGeocoded objects to sort by closest
     * distance to a point.
     * @param refPoint The reference point to sort closest to
     * @return A sorted list, from closest, to farthest away - of Geocoded items
     *
     * Some great ideas due to this nice article:
     * http://www.oracle.com/technetwork/articles/javase/generics-136597.html
     */
    public static <T extends IGeocoded> List<T> sortedByClosest(List<T> items, final Geolocation refPoint) {

        // An Linked List is used to do the sort ; the add() method get Override with the Distance metric logic
        List<T> sorted = new java.util.LinkedList<T>() {

            @Override
            public boolean add(T e) {
                // for an empty list, just insert the given element at the root
                if (this.size() == 0) {
                    super.addFirst(e);
                    return true;
                }

                // for the second element added, its a simple test
                if (this.size() == 1) {
                    double d0 = this.get(0).getGeolocation().getDistanceInKm(refPoint);
                    double dn = e.getGeolocation().getDistanceInKm(refPoint);
                    if (dn < d0) {
                        this.addFirst(e); // the new element is closer than the first, head of the list
                    } else {
                        this.addLast(e); // the new element is further than the first, end of the list
                    }
                    return true;
                }


                /*
                 * For the third, or later elements, we need to walk the list
                 * and compare the distances fore and aft. There is tricky logic
                 * here. The Unit Test code insures the behaviour is correct.
                 */
                boolean inserted = false;          // if all comparisons fail, slap the new entry on the end.
                final int range = this.size() - 1; // dont move into the for(...) decl otherwise infinite loop!

                for (int i = 0; i < range; i++) {
                    IGeocoded thisPosition = this.get(i);
                    IGeocoded nextPosition = this.get(i + 1);
                    double thisDistance = thisPosition.getGeolocation().getDistanceInKm(refPoint);
                    double nextDistance = nextPosition.getGeolocation().getDistanceInKm(refPoint);
                    double distance = e.getGeolocation().getDistanceInKm(refPoint);
                    if (thisDistance < distance && distance < nextDistance) {
                        this.add(i + 1, e);
                        inserted = true;
                    }
                }
                // If we've search the entire list, and the element still hasnt been inserted
                // Then, this element must the largest value yet.  Append it to the very end!
                if (!inserted) {
                    this.addLast(e);
                }
                return true;
            }
        };

        // Feed the items into the ordered list, forcing the ordering algorithm to do its thing
        for (T e : items) {
            sorted.add(e);
        }
        return sorted;
    }
}
