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
import org.simpleframework.xml.ElementList;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * A configuration composite that contains the total travel configuration for a
 * Route. Notice how common Route, Stop and Direction classes common to other
 * Webservice methods were distilled out.
 */
public class RouteConfiguration extends Route {

    private static final long serialVersionUID = -4526846496154559381L;

    /** UI color suggestion */
    @Attribute(name = "color")
    private UIColor uiColor;

    /** UI color suggestion */
    @Attribute(name = "oppositeColor")
    private UIColor uiOppositeColor;

    /** The service geography. */
    private ServiceArea serviceGeoArea;

    /** Stops on this Route */
    @ElementList(inline = true)
    private List<Stop> stops;

    /** Directions on this Route */
    @ElementList(inline = true)
    private List<Direction> directions;

    /** Map drawing points for UI */
    @ElementList(inline = true)
    private List<Path> paths;

    public RouteConfiguration(@Attribute(name = "color") UIColor uiColor,
                              @Attribute(name = "oppositeColor") UIColor uiOppositeColor,
                              @Attribute double latMin,
                              @Attribute double latMax,
                              @Attribute double lonMin,
                              @Attribute double lonMax,
                              @ElementList(inline = true) List<Stop> stops,
                              @ElementList(inline = true) List<Direction> directions,
                              @ElementList(inline = true) List<Path> paths) {
        this.uiColor = uiColor;
        this.uiOppositeColor = uiOppositeColor;
        this.serviceGeoArea = new ServiceArea(latMin, latMax, lonMin, lonMax);
        this.stops = stops;
        this.directions = directions;
        this.paths = paths;
    }

    /**
     * @return all of the Directions on this route
     */
    public List<Direction> getDirections() {
        return directions;
    }

    /**
     * @return the Paths for this route configuration
     */
    public List<Path> getPaths() {
        return paths;
    }

    /**
     *
     * @return the Stops for this route configuration
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     * @return the Service geography rectangle
     */
    public ServiceArea getServiceGeoArea() {
        return serviceGeoArea;
    }

    /**
     * @return the UI color from NextBus
     */
    public UIColor getUiColor() {
        return uiColor;
    }

    /**
     * @return the UI 'opposite color' from NextBus
     */
    public UIColor getUiOppositeColor() {
        return uiOppositeColor;
    }

    /**
     * A finder to retrieve a Direction by its tag;
     * useful for working with Schedule, Vehicle, and Prediction service calls.
     *
     * @param dirTag the tag for a direction
     * @return the Direction, if found
     * @throws IllegalArgumentException if no Direction is registered for the tag given
     */
    public Direction getDirectionById(String dirTag) {
        for (Direction d : directions)
            if (d.getTag().equals(dirTag))
                return d;
        throw new IllegalArgumentException("Direction instance for id=" + dirTag + " not found in this RouteConfiguration instance");
    }

    /**
     * A finder to retrieve a Stop by its tag;
     * useful for working with Schedule, Vehicle, and Prediction service calls.
     *
     * @param stopTag the tag for a stop
     * @return the Stop, if found
     * @throws IllegalArgumentException if no Stop is registered for the tag given
     */
    public Stop getStopById(String stopTag) {
        for (Stop s : stops)
            if (s.getTag().equals(stopTag))
                return s;
        throw new IllegalArgumentException("Stop instance for id=" + stopTag + " not found in this RouteConfiguration instance.");
    }

    @Override
    public String toString() {
        return "RouteConfiguration{" + "route=" + super.toString() + ", stops=" + stops.size() + ", directions=" + directions.size() + ", paths=" + paths.size() + ", serviceArea=" + serviceGeoArea + '}';
    }

    /**
     * NextBus sends advisory UI colors in the RouteConfig class and further
     * these come over the wire like this: color="006600"
     * oppositeColor="ffffff". Different platforms have different Color
     * configuration needs; While HTML styling using a simple RGB hex string,
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

        private static final long serialVersionUID = 4837641970386216280L;

        private String hexColor;
        private int red, green, blue;

        /**
         * Defaults to the #000000 color if unspecified.
         *
         * @param hexColor hex color string
         */
        public UIColor(String hexColor) {
            this.hexColor = (hexColor == null || hexColor.isEmpty()) ? "000000" : hexColor;

            String r = this.hexColor.substring(0, 2);
            String g = this.hexColor.substring(2, 4);
            String b = this.hexColor.substring(4, 6);

            red = Integer.parseInt(r, 16);
            green = Integer.parseInt(g, 16);
            blue = Integer.parseInt(b, 16);
        }

        /**
         * Gets the color code in HTML convention.
         *
         * @return e.g. '#00C410'
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

        private static final long serialVersionUID = 3910572710458275214L;

        private double latMin, latMax, lonMin, lonMax;

        public ServiceArea(double latMin, double latMax, double lonMin, double lonMax) {
            this.latMin = latMin;
            this.latMax = latMax;
            this.lonMin = lonMin;
            this.lonMax = lonMax;
        }

        /**
         * Returns the four corner geocodes of this service area square.
         *
         * @return an array of 4 geolocations
         */
        public Geolocation[] getGeosquare() {
            Geolocation corner1, corner2, corner3, corner4;
            corner1 = new Geolocation(latMin, lonMin);
            corner2 = new Geolocation(latMin, lonMax);
            corner3 = new Geolocation(latMax, lonMin);
            corner4 = new Geolocation(latMax, lonMax);
            return new Geolocation[]{corner1, corner2, corner3, corner4};
        }

        /**
         * Tests to see if some give Geolocation fits inside the bounded box of this service area.
         *
         * @param arg a geolocation
         * @return true iff the point is inside the box
         */
        public boolean isInTheBox(Geolocation arg) {
            double argLat = arg.getLatitude();
            double argLong = arg.getLongitude();
            return argLat >= latMin && argLat <= latMax && argLong >= lonMin && argLong <= lonMax;
        }

        @Override
        public String toString() {
            return "ServiceArea{" + Arrays.asList(getGeosquare()) + '}';
        }

    }

}
