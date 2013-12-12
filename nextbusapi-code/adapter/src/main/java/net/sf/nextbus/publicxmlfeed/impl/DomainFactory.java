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
package net.sf.nextbus.publicxmlfeed.impl;

import net.sf.nextbus.publicxmlfeed.domain.*;
import net.sf.nextbus.publicxmlfeed.service.TransientServiceException;
import net.sf.nextbus.publicxmlfeed.service.ValueConversionException;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.*;
import java.util.logging.Logger;

/**
 * Deserializes XML into Java Domain objects (POJOs).
 *
 * @author jrd
 */
public class DomainFactory {

    private final Serializer serializer;

    private static final Logger logger = Logger.getLogger(DomainFactory.class.getName());

    /**
     * Constructor
     */
    public DomainFactory() {
        serializer = new Persister();
    }

    /**
     * Deserializes the given agency list XML obtained from
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList</a>
     * and returns the list of agencies.
     *
     * @param xml agencyList XML
     * @return a list of agency POJOs
     * @throws Exception when XML cannot be deserialized
     */
    public List<Agency> getAgencies(String xml) throws Exception {
        AgencyList agencyList = serializer.read(AgencyList.class, xml);

        // If the remote web service has cast a defined exception
        if (agencyList.getError() != null) {
            throw new TransientServiceException(agencyList.getError().isShouldRetry(), agencyList.getError().getValue());
        }

        return agencyList.getList();
    }

    /**
     * Deserializes the given route list XML obtained from
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a=">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a=&lt;agency_tag&gt;</a>
     * and returns the list of routes.
     *
     * @param agency enclosing agency that owns the constructed routes
     * @param xml routeList XML
     * @return a list of route POJOs
     * @throws Exception when XML cannot be deserialized
     */
    public List<Route> getRoutes(Agency agency, String xml) throws Exception {
        RouteList routeList = serializer.read(RouteList.class, xml);

        // If the remote web service has cast a defined exception
        if (routeList.getError() != null)
            throw new TransientServiceException(routeList.getError().isShouldRetry(), routeList.getError().getValue());

        // Set the agency for the routes
        routeList.setAgency(agency);

        return routeList.getList();
    }

    /**
     * Deserializes the given route config XML obtained from
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=&r=">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=&lt;agency_tag&gt;&r=&lt;route tag&gt;</a>
     * and returns the route configuration for the given route.
     *
     * @param route route that owns the constructed route config
     * @param xml routeConfig xml
     * @return the route configuration for the given route
     * @throws Exception when XML cannot be deserialized
     */
    public RouteConfiguration getRouteConfiguration(Route route, String xml) throws Exception {
        RouteConfigurationList routeConfigurationList = serializer.read(RouteConfigurationList.class, xml);

        // If the remote web service has cast a defined exception
        if (routeConfigurationList.getError() != null)
            throw new TransientServiceException(routeConfigurationList.getError().isShouldRetry(), routeConfigurationList.getError().getValue());

        // If there is more than one route configuration in the list (XML was fetched without specifying route tag)
        if (routeConfigurationList.getList().size() > 1)
            // Search for the correct route config
            for (RouteConfiguration routeConfiguration : routeConfigurationList.getList())
                if (routeConfiguration.equals(route))
                    return routeConfiguration;

        // Otherwise, return the first and only route configuration
        return routeConfigurationList.getRouteConfiguration();
    }

    /**
     * Deserializes the given predictions XML obtained from
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a=&stopId=">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a=&lt;agency_tag&gt;&stopId=&lt;stop id&gt;</a>
     * or
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a=&r=&s=">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a=&lt;agency_tag&gt;&r=&lt;route tag&gt;&s=&lt;stop tag&gt;</a>
     * or
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=predictionsForMultiStops&a=&stops=&stops=&stops=">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=predictionsForMultiStops&a=&lt;agency_tag&gt;&stops=&lt;route tag&gt;|&lt;stop tag&gt;&stops=&lt;route tag&gt;|&lt;stop tag&gt;&stops=&lt;route tag&gt;|&lt;stop tag&gt;</a>
     * and returns the list of prediction groups.
     *
     * @param xml predictions XML
     * @return A PredictionGroup aggregator object and multiple Prediction POJOs
     * @throws Exception when XML cannot be deserialized
     */
    public List<PredictionGroup> getPredictions(String xml) throws Exception {
        PredictionGroupList predictionGroupList = serializer.read(PredictionGroupList.class, xml);

        // If the remote web service has cast a defined exception - propagate it as a service exception
        if (predictionGroupList.getError() != null) {
            throw new TransientServiceException(predictionGroupList.getError().isShouldRetry(), predictionGroupList.getError().getValue());
        }

        return predictionGroupList.getList();
    }

    /**
     * Deserializes the given schedule XML obtained from
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=schedule&a=&r=">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=schedule&a=&lt;agency_tag&gt;&r=&lt;route_tag&gt;</a>
     * and returns the list of
     *
     * @param route route
     * @param xml XML
     * @throws ValueConversionException Any additional data conversions
     * encountered AFTER XML Parsing.
     */
    public List<Schedule> getSchedule(Route route, String xml) {
        // parse the response XML
        net.sf.nextbus.publicxmlfeed.xjcgenerated.schedule.Body response = schedSvc.parse(xml);
        // Get the copyright notice which has to be loaded into every domain object we create.
        String cpyRt = response.getCopyright();
        // If the remote web service has cast a defined exception - propagate it as a service exception
        if (response.getError() != null) {
            throw new TransientServiceException(response.getError().isShouldRetry(), response.getError().getValue());
        }

        // Descend and build...
        List<Schedule> schedules = new ArrayList<Schedule>();
        for (net.sf.nextbus.publicxmlfeed.xjcgenerated.schedule.ScheduleRoute sr : response.getRoute()) {
            sr.getDirection();
            sr.getScheduleClass();
            sr.getServiceClass();
            sr.getTag();
            sr.getTitle();

            // Unpack the header - contains a list of stops by Id
            List<String> stopsById = new ArrayList<String>();
            for (net.sf.nextbus.publicxmlfeed.xjcgenerated.schedule.Headerstop hs : sr.getHeader().getStop()) {
                stopsById.add(hs.getTag());
            }

            // Unpack the TR blocks
            List<Schedule.Block> blocks = new ArrayList<Schedule.Block>();
            for (net.sf.nextbus.publicxmlfeed.xjcgenerated.schedule.Tr tr : sr.getTr()) {

                List<Schedule.Block.StopSchedule> stopScheds = new ArrayList<Schedule.Block.StopSchedule>();
                for (net.sf.nextbus.publicxmlfeed.xjcgenerated.schedule.Trstop trstop : tr.getStop()) {
                    Schedule.Block.StopSchedule st = null;
                    String stopTag = trstop.getTag();
                    if (trstop.getEpochTime() == -1 || (trstop.getValue() != null && trstop.getValue().equals("--"))) {
                        // This is a skipped stop
                        logger.fine("Found a skipped stop " + stopTag);
                        st = new Schedule.Block.SkippedStop(stopTag);
                    } else {
                        // convert the hh:mm:ss garbage
                        ScheduleCalendarTranslations.HMSConverter c = new ScheduleCalendarTranslations.HMSConverter(trstop.getValue());
                        // this is a regular stop
                        st = new Schedule.Block.StopScheduleTime(stopTag, c.getHh(), c.getMm());
                    }
                    stopScheds.add(st);
                }
                Schedule.Block block = new Schedule.Block(tr.getBlockID(), Collections.unmodifiableList(stopScheds));
                blocks.add(block);
            }
            Schedule schedule = new DailySchedule(route,
                    Collections.unmodifiableList(stopsById),
                    Collections.unmodifiableList(blocks),
                    sr.getScheduleClass(), sr.getServiceClass(), sr.getDirection());
            schedules.add(schedule);
        }
        return schedules;
    }

    // TODO Messages

    /**
     * Deserializes the given vehicle locations XML obtained from
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=vehicleLocations&a=&r=&t=">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=vehicleLocations&a=&lt;agency_tag&gt;&r=&lt;route tag&gt;&t=&lt;epoch time in msec&gt;</a>
     * and returns the list of vehicle locations.
     *
     * @param xml vehicleLocations XML
     * @return Vehicle POJOs
     * @throws Exception when XML cannot be deserialized
     */
    public List<Vehicle> getVehicleLocations(String xml) throws Exception {
        VehicleList vehicleList = serializer.read(VehicleList.class, xml);

        // If the remote web service has cast a defined exception
        if (vehicleList.getError() != null) {
            throw new TransientServiceException(vehicleList.getError().isShouldRetry(), vehicleList.getError().getValue());
        }

        return vehicleList.getList();
    }

}
