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

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

/**
 * <p>
 * A configuration composite that contains the total travel configuration for a
 * Route. Notice how common Route, Stop and Direction classes common to other
 * Webservice methods were distilled out.
 * </p><img src="doc-files/RouteConfig.png"/>
 *
 * @author jrd
 */
public class RouteConfiguration extends NextBusValueObject {

    static final long serialVersionUID = -4628560559639733708L;
    /** The Route for this configuration composite. */
    protected Route route;
    /** The service geography. */
    protected ServiceArea serviceGeoArea;
    /** UI color suggestion */
    protected UIColor uiAdviceOppositeColor;
    /** UI color suggestion */
    protected UIColor uiAdviceColor;
    /** Stops on this Route */
    protected List<Stop> stops;
    /** Directions on this Route */
    protected List<Direction> directions;
    /** Map drawing points for UI */
    protected List<Path> paths;

    

    /**
     * Serialization ctor.
     */
    protected RouteConfiguration() {
    }

    /**
     * Domain factory ctor.
     *
     * @param parent containing Route
     * @param _stops
     * @param _directions
     * @param _paths
     * @param _serviceGeoArea
     * @param color
     * @param oppositeColor
     */
    public RouteConfiguration(Route parent, List<Stop> _stops, List<Direction> _directions, List<Path> _paths, ServiceArea _serviceGeoArea, UIColor oppositeColor, UIColor color, String copyright) {
        super(copyright);
        route = parent;
        stops = Collections.unmodifiableList(_stops);
        directions = Collections.unmodifiableList(_directions);
        paths = Collections.unmodifiableList(_paths);
        serviceGeoArea = _serviceGeoArea;
        uiAdviceColor = color;
        uiAdviceOppositeColor = oppositeColor;
    }

    /**
     * The Route is the identity element, or owner, for instances of this class.
     *
     * @return The Route that owns this configuration data.
     */
    public Route getRoute() {
        return route;
    }

    /**
     *
     * @return All of the Route Directions on this Route.
     */
    public List<Direction> getDirections() {
        return directions;
    }

    /**
     *
     * @return the Path Elements for this route configuration.
     */
    public List<Path> getPaths() {
        return paths;
    }

    /**
     *
     * @return the Stops for this route configuration.
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     *
     * @return the Service geography rectangle
     */
    public ServiceArea getServiceGeoArea() {
        return serviceGeoArea;
    }

    /**
     *
     * @return the color advice from NextBus
     */
    public UIColor getUiAdviceColor() {
        return uiAdviceColor;
    }

    /**
     *
     * @return the 'opposite color' advice from NextBus
     */
    public UIColor getUiAdviceOppositeColor() {
        return uiAdviceOppositeColor;
    }

    /**
     * A finder to retrieve a Direction by its ID ; useful for working with
     * Schedule, VehicleLocation and Prediction service calls.
     *
     * @param directionId
     * @return instance of Direction, if found.
     * @exception IllegalArgumentException if no Direction is registered for the
     * ID given.
     */
    public Direction getDirectionById(String directionId) {
        for (Direction d : directions) {
            if (d.getTag().equals(directionId)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Direction instance for id=" + directionId + " not found in this RouteConfiguration instance");
    }

    /**
     * A finder to retrieve a Stop by its ID ; useful for working with Schedule,
     * Vehicle Location and Prediction service calls.
     *
     * @param stopId
     * @return Stop
     */
    public Stop getStopById(String stopId) {
        for (Stop s : stops) {
            if (s.getTag().equals(stopId)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Stop instance for id=" + stopId + " not found in this RouteConfiguration instance.");

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RouteConfiguration other = (RouteConfiguration) obj;
        if (this.route != other.route && (this.route == null || !this.route.equals(other.route))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.route != null ? this.route.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "RouteConfiguration{" + "route=" + route + ", stops=" + stops.size() + ", directions=" + directions.size() + ", paths=" + paths.size() + ", serviceArea=" + serviceGeoArea + '}';
    }
    
    /**
     * Nextbus sends advisory UI colors in the RouteConfig class and further
     * these come over the wire like this: color="006600"
     * oppositeColor="ffffff". Different platforms have different Color
     * configuration needs; While html styling using a simple RGB hex string,
     * other UI such as Java Swing and Android rely on more formal classes that
     * combine Color Space with R/G/B values.
     *
     * You can over-engineer domain classes for sure, but this design choice is
     * really about 'doing the right thing especially when it's 5 minutes of
     * your time' to promote the likely adoption and use of the project by other
     * developers.
     *
     * @author jrd
     */
    public static class UIColor implements Serializable {

        public static final long serialVersionUID = -7269390870711698813L;
        protected String hexColor = "000000";
        protected int red, green, blue = 0;

        protected UIColor() {
        }

        ;
    /**
     * Defaults to the #000000 color if unspecified.
     * 
     * @param _hexColor hex color string 
     */
    public UIColor(String _hexColor) {
            if (_hexColor == null || _hexColor.isEmpty()) {
                return;
            }
            this.hexColor=_hexColor;

            String r = hexColor.substring(0, 2);
            String g = hexColor.substring(2, 4);
            String b = hexColor.substring(4, 6);

            red = Integer.parseInt(r, 16);
            green = Integer.parseInt(g, 16);
            blue = Integer.parseInt(b, 16);
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
         * 
         * @return the sRGB Color Value.
         */
        public int getRGBColor() {
            return Integer.parseInt(hexColor);
        }

        /**
         * For JFC Swing or Android folks.
         *
         * @return The Blue channel color.
         */
        public int getBlue() {
            return blue;
        }

        /**
         * For JFC Swing or Android folks.
         *
         * @return The Green channel color.
         */
        public int getGreen() {
            return green;
        }

        /**
         * For JFC Swing or Android folks.
         *
         * @return The Red channel color;
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
public static class ServiceArea implements Serializable {

    public static final long serialVersionUID = -2726681093391741083L;
    
    protected double latMin, latMax, longMin, longMax;

    protected ServiceArea() {
    }

    public ServiceArea(double _latMin, double _latMax, double _longMin, double _longMax) {
        this.latMin = _latMin;
        this.latMax = _latMax;
        this.longMin = _longMin;
        this.longMax = _longMax;

    }

    /**
     * Returns the four corner geocodes of this service area square.
     *
     * @return an array of 4 geolocations
     */
    public Geolocation[] getGeosquare() {
        Geolocation corner1, corner2, corner3, corner4;
        corner1 = new Geolocation(latMin, longMin);
        corner2 = new Geolocation(latMin, longMax);
        corner3 = new Geolocation(latMax, longMin);
        corner4 = new Geolocation(latMax, longMax);
        return new Geolocation[]{corner1, corner2, corner3, corner4};
    }

    private boolean testRg(double test, double min, double max) {
        return (test <= max && min <= min);
    }

    /**
     * Tests to see if some give Geolocation fits inside the bounded box of this service area.
     * 
     * @param arg a geolocation
     * 
     * @return true if the point is inside the box, false if it is not
     */
    public boolean isInTheBox(Geolocation arg) {
        return testRg(arg.getLatitude(), latMin, latMax) && testRg(arg.getLongitude(), longMin, longMax);
    }

    @Override
    public String toString() {
        return "ServiceArea{" + Arrays.asList(getGeosquare()) + '}';
    }
    
    
}

}
