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
package com.elliottsj.nextbus.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

/**
 * <p>
 *     A configuration composite that contains the total travel configuration for a
 *     Route. Notice how common Route, Stop and Direction classes common to other
 *     Webservice methods were distilled out.
 * </p>
 *
 * <p>
 *     Routes may have multiple Directions, each which further have Stops that can
 *     be either shared, or disjoint, to other Routes.
 * </p>
 *
 * <p>
 *     Transit systems have routes which may further enclose Directions, Stops, have
 *     assigned Vehicles and also have posted Schedules. This class reflects the
 *     metadata conveyed in the various XML Response Streams of NextBus.
 * </p>
 *
 * @author jrd
 * @author elliottsj
 */
@SuppressWarnings("UnusedDeclaration")
public class RouteConfiguration extends NextbusValueObject {

    private static final long serialVersionUID = -7501539020969440154L;

    /** The Route for this configuration composite. */
    protected Route route;

    /** The service geography. */
    protected ServiceArea serviceArea;

    /** UI color suggestion */
    protected UIColor uiColor;

    /** UI color suggestion */
    protected UIColor uiOppositeColor;

    /** Stops on this Route */
    protected List<Stop> stops;

    /** Directions on this Route */
    protected List<Direction> directions;

    /** Map drawing points for UI */
    protected List<Path> paths;

    /**
     * Full constructor
     *
     * @param route route that owns this configuration
     * @param stops stops on this route (stops can be shared between routes)
     * @param directions directions on this route
     * @param paths paths on this route
     * @param serviceArea the service area this route covers
     * @param uiColor the recommended UI color
     * @param uiOppositeColor the recommended opposite UI color
     * @param copyright copyright text provided by NextBus
     * @param timestamp epoch milliseconds when this route configuration was created
     */
    public RouteConfiguration(Route route, List<Stop> stops, List<Direction> directions, List<Path> paths, ServiceArea serviceArea, UIColor uiColor, UIColor uiOppositeColor, String copyright, Long timestamp) {
        super(copyright, timestamp);
        this.route = route;
        this.stops = Collections.unmodifiableList(stops);
        this.directions = Collections.unmodifiableList(directions);
        this.paths = Collections.unmodifiableList(paths);
        this.serviceArea = serviceArea;
        this.uiColor = uiColor;
        this.uiOppositeColor = uiOppositeColor;
    }

    /**
     * Domain factory constructor.
     */
    public RouteConfiguration(Route route, List<Stop> stops, List<Direction> directions, List<Path> paths, ServiceArea serviceArea, UIColor uiColor, UIColor uiOppositeColor, String copyright) {
        this(route, stops, directions, paths, serviceArea, uiColor, uiOppositeColor, copyright, null);
    }

    /**
     * Gets the owning route for this configuration.
     *
     * @return the route that owns this configuration data
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Gets the directions for this route configuration.
     *
     * @return all of the route directions on this route
     */
    public List<Direction> getDirections() {
        return directions;
    }

    /**
     * Gets the map paths for this route configuration.
     *
     * @return the path elements for this route configuration
     */
    public List<Path> getPaths() {
        return paths;
    }

    /**
     * Gets the stops for this route configuration.
     *
     * @return the stops for this route configuration
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     * Gets the service area rectangle for this route configuration.
     *
     * @return the service geography rectangle
     */
    public ServiceArea getServiceArea() {
        return serviceArea;
    }

    /**
     * Gets the recommended UI color for this route configuration.
     *
     * @return the recommended UI color for this route configuration
     */
    public UIColor getUiColor() {
        return uiColor;
    }

    /**
     * Gets the recommended opposite UI color for this route configuration.
     *
     * @return the recommended opposite UI color for this route configuration
     */
    public UIColor getUiOppositeColor() {
        return uiOppositeColor;
    }

    /**
     * Utility finder to retrieve a direction by its tag; useful for working with
     * Schedule, VehicleLocation and Prediction service calls.
     *
     * @param tag the tag of the direction to get
     * @return the direction with the given tag, if found
     * @exception IllegalArgumentException if no direction is registered for the tag given.
     */
    public Direction getDirectionByTag(String tag) {
        for (Direction direction : directions)
            if (direction.getTag().equals(tag))
                return direction;
        throw new IllegalArgumentException("Direction instance for tag=" + tag + " not found in this RouteConfiguration instance");
    }

    /**
     * A finder to retrieve a stop by its tag; useful for working with Schedule,
     * Vehicle Location and Prediction service calls.
     *
     * @param tag the tag of the stop to get
     * @return the stop with the given tag, if found
     */
    public Stop getStopByTag(String tag) {
        for (Stop stop : stops)
            if (stop.getTag().equals(tag))
                return stop;
        throw new IllegalArgumentException("Stop instance for id=" + tag + " not found in this RouteConfiguration instance.");
    }

    /**
     * RouteConfiguration identity is composite of the route.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteConfiguration)) return false;

        RouteConfiguration routeConfiguration = (RouteConfiguration) o;

        return route.equals(routeConfiguration.route);
    }

    @Override
    public int hashCode() {
        return route.hashCode();
    }

    @Override
    public String toString() {
        return "RouteConfiguration{" + "route=" + route + ", stops=" + stops.size() + ", directions=" + directions.size() + ", paths=" + paths.size() + ", serviceArea=" + serviceArea + '}';
    }
    
    /**
     * Nextbus sends advisory UI colors in the RouteConfig class and further
     * these come over the wire like this: color="006600"
     * oppositeColor="ffffff". Different platforms have different color
     * configuration needs; while html styling using a simple RGB hex string,
     * other UI such as Java Swing and Android rely on more formal classes that
     * combine color space with R/G/B values.
     *
     * You can over-engineer domain classes for sure, but this design choice is
     * really about 'doing the right thing especially when it's 5 minutes of
     * your time' to promote the likely adoption and use of the project by other
     * developers.
     *
     * @author jrd
     * @author elliottsj
     */
    @SuppressWarnings("UnusedDeclaration")
    public static class UIColor implements Serializable {

        private static final long serialVersionUID = -770055738749133793L;

        protected String hexColor;
        protected int red, green, blue;

        /**
         * Constructs this color with the given hex color, or {@code #000000} if unspecified.
         *
         * @param hexColor hex color string, i.e. {@code "ff0000"}
         */
        public UIColor(String hexColor) {
            this.hexColor = hexColor != null && !hexColor.isEmpty() ? hexColor : "000000";

            String r = this.hexColor.substring(0, 2);
            String g = this.hexColor.substring(2, 4);
            String b = this.hexColor.substring(4, 6);

            this.red = Integer.parseInt(r, 16);
            this.green = Integer.parseInt(g, 16);
            this.blue = Integer.parseInt(b, 16);
        }

        public String getHexColor() {
            return hexColor;
        }

        /**
         * Gets the color code in HTML convention.
         *
         * @return i.e. '#00C410'
         */
        public String getHtmlColorCode() {
            return '#' + hexColor;
        }

        /**
         * @return the sRGB Color Value.
         */
        public int getRGBColor() {
            return Integer.parseInt(hexColor);
        }

        /**
         * For JFC Swing or Android folks.
         *
         * @return the blue channel color
         */
        public int getBlue() {
            return blue;
        }

        /**
         * For JFC Swing or Android folks.
         *
         * @return the green channel color
         */
        public int getGreen() {
            return green;
        }

        /**
         * For JFC Swing or Android folks.
         *
         * @return the red channel color
         */
        public int getRed() {
            return red;
        }

        @Override
        public String toString() {
            return "UIColor{" + "red=" + red + ", green=" + green + ", blue=" + blue + '}';
        }
    }

    /**
     * RouteConfiguration defines a geographic square for each route's service area.
     * This class cleans that information up nicely as well.
     *
     * @author jrd
     */
    @SuppressWarnings("UnusedDeclaration")
    public static class ServiceArea implements Serializable {

        private static final long serialVersionUID = -2923907330197817961L;

        protected double latMin, latMax, lonMin, lonMax;

        /**
         * Constructs this service area with the given bounds
         *
         * @param latMin minimum latitude
         * @param latMax maximum latitude
         * @param lonMin minimum longitude
         * @param lonMax maximum longitude
         */
        public ServiceArea(double latMin, double latMax, double lonMin, double lonMax) {
            this.latMin = latMin;
            this.latMax = latMax;
            this.lonMin = lonMin;
            this.lonMax = lonMax;
        }

        public double getLatMin() {
            return latMin;
        }

        public double getLatMax() {
            return latMax;
        }

        public double getLonMin() {
            return lonMin;
        }

        public double getLonMax() {
            return lonMax;
        }

        /**
         * Returns the four corner geocodes of this service area square.
         *
         * @return an array of 4 geolocations
         */
        public Geolocation[] getGeosquare() {
            return new Geolocation[] { new Geolocation(latMin, lonMin),
                                       new Geolocation(latMin, lonMax),
                                       new Geolocation(latMax, lonMin),
                                       new Geolocation(latMax, lonMax) };
        }

        /**
         * Tests to see if some give Geolocation fits inside the bounded box of this service area.
         *
         * @param point a geolocation
         * @return true iff the point is inside the box
         */
        public boolean isInTheBox(Geolocation point) {
            double lat = point.getLatitude();
            double lon = point.getLongitude();
            return lat >= latMin && lat <= latMax && lon >= lonMin && lon <= lonMax;
        }

        @Override
        public String toString() {
            return "ServiceArea{" + Arrays.asList(getGeosquare()) + '}';
        }

    }

}
