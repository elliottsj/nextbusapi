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
import net.sf.nextbus.publicxmlfeed.impl.simplexml.agencylist.AgencyListBody;
import net.sf.nextbus.publicxmlfeed.impl.simplexml.predictions.Predictions;
import net.sf.nextbus.publicxmlfeed.impl.simplexml.predictions.PredictionsBody;
import net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig.Point;
import net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig.RouteConfigBody;
import net.sf.nextbus.publicxmlfeed.impl.simplexml.routelist.RouteListBody;
import net.sf.nextbus.publicxmlfeed.impl.simplexml.schedule.Block;
import net.sf.nextbus.publicxmlfeed.impl.simplexml.schedule.ScheduleBody;
import net.sf.nextbus.publicxmlfeed.impl.simplexml.vehiclelocations.VehicleLocationsBody;
import net.sf.nextbus.publicxmlfeed.service.TransientServiceException;
import net.sf.nextbus.publicxmlfeed.service.ValueConversionException;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.*;
import java.util.logging.Logger;

/**
 * Converts XML into SimpleXML objects into Java Domain objects.
 *
 * @author jrd
 * @author elliottsj
 */
public class DomainFactory {

    private static final Logger logger = Logger.getLogger(DomainFactory.class.getName());
    private Serializer mSerializer;

    /**
     * Constructs this domain factory.
     */
    public DomainFactory() {
        mSerializer = new Persister();
    }

    /**
     * Builds agency domain objects from the given XML string returned by a request to
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList
     * </a>.
     *
     * @param xml the XML from a request to NextBus
     * @return a list of agencies
     */
    public List<Agency> getAgencies(String xml) throws Exception {
        List<Agency> agencies = new ArrayList<Agency>();

        AgencyListBody response = mSerializer.read(AgencyListBody.class, xml);

        // If the remote web service has cast a defined exception
        if (response.getError() != null)
            throw new TransientServiceException(response.getError().isShouldRetry(), response.getError().getValue());

        for (net.sf.nextbus.publicxmlfeed.impl.simplexml.agencylist.Agency a : response.getAgencies())
            agencies.add(new Agency(a.getTag(), a.getTitle(), a.getShortTitle(), a.getRegionTitle(), response.getCopyright()));

        return agencies;
    }

    /**
     * Builds route domain objects from the given XML string returned by a request to
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a=">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a=&lt;agency_tag&gt;
     * </a>
     *
     * @param agency agency that owns the constructed routes
     * @param xml the XML from a request to NextBus
     * @return a list of routes
     */
    public List<Route> getRoutes(Agency agency, String xml) throws Exception {
        List<Route> routes = new ArrayList<Route>();

        RouteListBody response = mSerializer.read(RouteListBody.class, xml);

        // If the remote web service has cast a defined exception
        if (response.getError() != null)
            throw new TransientServiceException(response.getError().isShouldRetry(), response.getError().getValue());

        for (net.sf.nextbus.publicxmlfeed.impl.simplexml.routelist.Route r : response.getRoutes())
            routes.add(new Route(agency, r.getTag(), r.getTitle(), r.getShortTitle(), response.getCopyright()));

        return routes;
    }

    /**
     * Builds route configuration domain objects from the given XML string returned by a request to
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=&r=">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=&lt;agency_tag&gt;&r=&lt;route_tag&gt;
     * </a>
     *
     * @param route route that owns the route configuration
     * @param xml routeConfig xml
     * @return the configuration for the given route
     */
    public RouteConfiguration getRouteConfiguration(Route route, String xml) throws Exception {
        List<Stop> stops = new ArrayList<Stop>();
        List<Direction> directions = new ArrayList<Direction>();
        List<Path> paths = new ArrayList<Path>();

        RouteConfigBody response = mSerializer.read(RouteConfigBody.class, xml);

        // If the remote web service has cast a defined exception
        if (response.getError() != null)
            throw new TransientServiceException(response.getError().isShouldRetry(), response.getError().getValue());

        String copyright = response.getCopyright();
        net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig.RouteConfiguration wireRouteConfiguration = response.getRoutes().get(0);

        // Build a map of stops, then reuse it to build the directions
        Map<String, Stop> stopsByTag = new HashMap<String, Stop>();

        for (net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig.Stop s : wireRouteConfiguration.getStops()) {
            Geolocation gps = new Geolocation(s.getLat(), s.getLon());
            Stop stop = new Stop(route.getAgency(), s.getTag(), s.getTitle(), s.getShortTitle(), s.getStopId(), gps, copyright);
            stops.add(stop);
            stopsByTag.put(s.getTag(), stop);
        }

        // NextBus does not provide unique identifiers for paths, so we provide out own
        int pathId = 0;
        for (net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig.Path p : wireRouteConfiguration.getPaths()) {
            List<Geolocation> points = new ArrayList<Geolocation>();
            for (Point point : p.getPoints())
                points.add(new Geolocation(point.getLat(), point.getLon()));
            paths.add(new Path(route, Integer.toString(pathId), points, copyright));
            pathId++;
        }

        for (net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig.Direction d : wireRouteConfiguration.getDirections()) {
            List<Stop> directionStops = new ArrayList<Stop>();
            for (net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig.Direction.Stop ds : d.getStops())
                directionStops.add(stopsByTag.get(ds.getTag()));
            List<Stop> unmodifiableStops = Collections.unmodifiableList(directionStops);
            Direction direction = new Direction(route, d.getTag(), d.getTitle(), d.getName(), unmodifiableStops, copyright);
            directions.add(direction);
        }

        RouteConfiguration.ServiceArea serviceArea = new RouteConfiguration.ServiceArea(wireRouteConfiguration.getLatMin(),
                                                                                        wireRouteConfiguration.getLatMax(),
                                                                                        wireRouteConfiguration.getLonMin(),
                                                                                        wireRouteConfiguration.getLonMax());
        RouteConfiguration.UIColor color = new RouteConfiguration.UIColor(wireRouteConfiguration.getColor());
        RouteConfiguration.UIColor oppositeColor = new RouteConfiguration.UIColor(wireRouteConfiguration.getOppositeColor());

        return new RouteConfiguration(route, stops, directions, paths, serviceArea, color, oppositeColor, copyright);
    }

    /**
     * Builds prediction domain objects from the given XML string returned by a request to
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=predictionsForMultiStops&a=&stops=&stops=">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=predictionsForMultiStops&a=&lt;agency_tag&gt;&stops=&lt;route_tag&gt;|&lt;stop_tag&gt;&stops=&lt;route_tag&gt;|&lt;stop_tag&gt;
     * </a>
     *
     * @param stops stops to find predictions for
     * @param xml predictions or predictionsForMultiStops XML
     * @return a PredictionGroup aggregator object
     */
    public List<PredictionGroup> getPredictions(Collection<Stop> stops, String xml) throws Exception {
        // Create a lookup table and verify that each stop has the same agency
        Agency agency = null;
        Map<String, Stop> stopsByTag = new HashMap<String, Stop>();
        for (Stop stop : stops) {
            stopsByTag.put(stop.getTag(), stop);
            if (agency == null)
                agency = stop.getAgency();
            else
                assert(stop.getAgency().equals(agency));
        }

        PredictionsBody response = mSerializer.read(PredictionsBody.class, xml);

        String copyright = response.getCopyright();

        // If the remote web service has cast a defined exception - propagate it as a service exception
        if (response.getError() != null)
            throw new TransientServiceException(response.getError().isShouldRetry(), response.getError().getValue());

        // Recurse deep into a nested parsed XML and converted to nested collections of POJOs!

        List<PredictionGroup> predictionGroups = new ArrayList<PredictionGroup>();

        for (Predictions wirePredictions : response.getPredictions()) {
            Route route = new Route(agency, wirePredictions.getRouteTag(), wirePredictions.getRouteTitle(), null, response.getCopyright());

            List<String> messages = new ArrayList<String>();

            for (net.sf.nextbus.publicxmlfeed.impl.simplexml.predictions.Message m : wirePredictions.getMessages())
                messages.add(m.getText());

            List<PredictionGroup.PredictionDirection> predictionDirections = new ArrayList<PredictionGroup.PredictionDirection>();

            for (net.sf.nextbus.publicxmlfeed.impl.simplexml.predictions.Direction d : wirePredictions.getDirections()) {
                List<Prediction> directionPredictions = new ArrayList<Prediction>();

                for (net.sf.nextbus.publicxmlfeed.impl.simplexml.predictions.Prediction wirePrediction : d.getPredictions())
                    directionPredictions.add(new Prediction(route,
                                                            stopsByTag.get(wirePredictions.getStopTag()),
                                                            wirePrediction.getVehicle(),
                                                            wirePrediction.getDirTag(),
                                                            wirePrediction.isDeparture(),
                                                            wirePrediction.isAffectedByLayover(),
                                                            wirePrediction.getTripTag(),
                                                            wirePrediction.getBlock(),
                                                            wirePrediction.getEpochTime(),
                                                            copyright,
                                                            wirePrediction.isDelayed(),
                                                            wirePrediction.isScheduleBased()));

                predictionDirections.add(new PredictionGroup.PredictionDirection(d.getTitle(),
                                                                Collections.unmodifiableList(directionPredictions)));
            }

            predictionGroups.add(new PredictionGroup(route,
                                                     stopsByTag.get(wirePredictions.getStopTag()),
                                                     Collections.unmodifiableList(predictionDirections),
                                                     Collections.unmodifiableList(messages),
                                                     copyright));
        }

        return predictionGroups;
    }

    /**
     * Builds route configuration domain objects from the given XML string returned by a request to
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=&r=">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=&lt;agency_tag&gt;&r=&lt;route_tag&gt;
     * </a>
     *
     * @param route route that owns the vehicles
     * @param xml vehicleLocations XML
     * @return a list of vehicle locations
     */
    public List<VehicleLocation> getVehicleLocations(Route route, String xml) throws Exception {
        List<VehicleLocation> vehicleLocations = new ArrayList<VehicleLocation>();

        VehicleLocationsBody response = mSerializer.read(VehicleLocationsBody.class, xml);

        // If the remote web service has cast a defined exception
        if (response.getError() != null)
            throw new TransientServiceException(response.getError().isShouldRetry(), response.getError().getValue());

        String copyright = response.getCopyright();

        for (net.sf.nextbus.publicxmlfeed.impl.simplexml.vehiclelocations.Vehicle v : response.getVehicles()) {
            Geolocation lastPosition = new Geolocation(v.getLat(), v.getLon());

            // Adjust the last time by the 'seconds Since Last Report' attribute of the message.
            long lastTime = System.currentTimeMillis() - v.getSecsSinceReport() * 1000;

            vehicleLocations.add(new VehicleLocation(route,
                                                     v.getId(),
                                                     v.getDirTag(),
                                                     v.isPredictable(),
                                                     lastPosition,
                                                     lastTime,
                                                     v.getSpeedKmHr(),
                                                     v.getHeading(),
                                                     copyright));
        }
        return vehicleLocations;
    }

    /**
     * Builds schedule domain objects from the given XML string returned by a request to
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=&r=">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=&lt;agency_tag&gt;&r=&lt;route_tag&gt;
     * </a>
     *
     * @param route route
     * @param xml XML
     * @throws ValueConversionException Any additional data conversions
     * encountered AFTER XML Parsing.
     */
    public List<Schedule> getSchedule(Route route, String xml) throws Exception {
        ScheduleBody response = mSerializer.read(ScheduleBody.class, xml);

        // If the remote web service has cast a defined exception - propagate it as a service exception
        if (response.getError() != null)
            throw new TransientServiceException(response.getError().isShouldRetry(), response.getError().getValue());

        List<Schedule> schedules = new ArrayList<Schedule>();
        for (net.sf.nextbus.publicxmlfeed.impl.simplexml.schedule.Route r : response.getRoutes()) {

            List<String> stopsByTag = new ArrayList<String>();
            for (net.sf.nextbus.publicxmlfeed.impl.simplexml.schedule.Stop hs : r.getHeader())
                stopsByTag.add(hs.getTag());

            // Unpack the TR blocks
            List<Schedule.Block> blocks = new ArrayList<Schedule.Block>();
            for (Block tr : r.getBlocks()) {

                List<Schedule.Block.StopSchedule> stopScheds = new ArrayList<Schedule.Block.StopSchedule>();
                for (Block.Stop trstop : tr.getStop()) {
                    Schedule.Block.StopSchedule st;
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
                    Collections.unmodifiableList(stopsByTag),
                    Collections.unmodifiableList(blocks),
                    r.getScheduleClass(), r.getServiceClass(), r.getDirection(), response.getCopyright());
            schedules.add(schedule);
        }
        return schedules;
    }

    // TODO: Messages

}
