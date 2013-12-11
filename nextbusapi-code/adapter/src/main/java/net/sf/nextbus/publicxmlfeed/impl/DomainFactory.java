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
import net.sf.nextbus.publicxmlfeed.service.ServiceConfigurationException;
import net.sf.nextbus.publicxmlfeed.service.TransientServiceException;
import net.sf.nextbus.publicxmlfeed.service.ValueConversionException;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.xml.sax.SAXException;

import java.util.*;
import java.util.logging.Level;
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
     * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a=&r=&s=">
     *     http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a=&lt;agency_tag&gt;&r=&lt;route tag&gt;&s=&lt;stop tag&gt;</a>
     * and returns the predictions for the given stops
     *
     * @param stops
     * @param xml predictions XML
     * @return A PredictionGroup aggregator object and multiple Prediction POJOs
     */
    public List<PredictionGroup> getPredictions(Collection<Stop> stops, String xml) {

        // create a lookup table by Stop tag
        Agency a = null;
        Map<String, Stop> stopsByTag = new HashMap<String, Stop>();
        for (Stop stop : stops) {
            stopsByTag.put(stop.getTag(), stop);
            if (a==null) a=stop.getAgency();
            assert(stop.getAgency().equals(a)); // XXX throw exception
        }

        // parse the response XML
        net.sf.nextbus.publicxmlfeed.xjcgenerated.prediction.Body response = predictionSvc.parse(xml);

        // Get the copyright notice which has to be loaded into every domain object we create.
        String cpyRt = response.getCopyright();
        // If the remote web service has cast a defined exception - propagate it as a service exception
        if (response.getError() != null) {
            throw new TransientServiceException(response.getError().isShouldRetry(), response.getError().getValue());
        }

        List<PredictionGroup> returnVal = new ArrayList<PredictionGroup>();

        //
        // Here we go - Recurse deep into a Nested parsed XML and converted to Nested Collections of POJOs!
        //

        // For each of the Predictions nodes (there are multiple for the predictionsMultiStop RPC.
        for (net.sf.nextbus.publicxmlfeed.xjcgenerated.prediction.Predictions pns : response.getPredictions()) {
            // each <predictions> group has a Route and Stop associated with it...
            Route r = new Route(a, pns.getRouteTag(), pns.getRouteTitle(), response.getCopyright());


            List<Prediction> predictions = new ArrayList<Prediction>();
            List<String> messages = new ArrayList<String>();

            // Construct an new enclosing Prediction Group.  First, gather any messages
            List<String> msgs = new ArrayList<String>();
            for (net.sf.nextbus.publicxmlfeed.xjcgenerated.prediction.Message m : pns.getMessage()) {
                msgs.add(m.getText());
            }

            List<PredictionGroup.PredictionDirection> dns = new ArrayList<PredictionGroup.PredictionDirection>();

            // Descend into the Direction nodes
            for (net.sf.nextbus.publicxmlfeed.xjcgenerated.prediction.Direction d : pns.getDirection()) {
                // back-track a Stop class by ID
                Stop s = stopsByTag.get(pns.getStopTag());

                // Descend into the Prediction elements with each Direction
                List<Prediction> pdns4Dirn = new ArrayList<Prediction>();
                for (net.sf.nextbus.publicxmlfeed.xjcgenerated.prediction.Direction.Prediction p : d.getPrediction()) {

                    // Stupidity checks are needed - sometimes the vendor fails to send required fields, as in the
                    // case of isAffectedByLayover which occassional is absent, turning into a NullPointerException.

                    boolean layover = false;
                    if (p.isAffectedByLayover() != null) {
                        layover = p.isAffectedByLayover();
                    }
                    boolean delayed = false;
                    if (p.isDelayed() != null) {
                        delayed = p.isDelayed();
                    }
                    boolean schedBased = false;
                    if (p.isIsScheduleBased() != null) {
                        schedBased = p.isIsScheduleBased();
                    }

                    // build a new Prediction POJO
                    Prediction pn = new Prediction(r, s,
                                                   p.getVehicle(),
                                                   p.getDirTag(),
                                                   p.isIsDeparture().booleanValue(),
                                                   layover,
                                                   p.getTripTag(),
                                                   p.getBlock(),
                                                   p.getEpochTime(),
                                                   cpyRt,
                                                   delayed,
                                                   schedBased
                    );
                    // Add the Prediction POJO to the Direction list
                    pdns4Dirn.add(pn);
                }

                // We now have have to constuct an immutable PredictionGroup::Direction POJO, do so...
                PredictionGroup.PredictionDirection pd = new PredictionGroup.PredictionDirection(d.getTitle(), Collections.unmodifiableList(pdns4Dirn));

                // Add the PredictionGroup::Direction POJO to the List of Directions
                dns.add(pd);
            }

            // Done - now build the outermost enclosing POJO - the Prediction Group
            Stop s = stopsByTag.get(pns.getStopTag());

            PredictionGroup pg = new PredictionGroup(r, s,
                                                     Collections.unmodifiableList(dns),
                                                     cpyRt,
                                                     Collections.unmodifiableList(msgs));

            // Add the PredictionGroup to the result
            returnVal.add(pg);
        }

        return returnVal;
    }

    /**
     * Deserializes the given vehicle locations XML obtained from
     *
     * @param route Route (and nested Agency) that owns the constructed domain
     * objects.
     * @param xml vehicleLocations xml
     * @return VehicleLocation POJOs
     */
    public List<VehicleLocation> getVehicleLocations(Route route, String xml) {
        List<VehicleLocation> returnValue = new ArrayList<VehicleLocation>();

        // parse the response XML
        net.sf.nextbus.publicxmlfeed.xjcgenerated.vehiclelocation.Body response = vehicleLocnSvc.parse(xml);
        // If the remote web service has cast a defined exception
        if (response.getError() != null) {
            throw new TransientServiceException(response.getError().isShouldRetry(), response.getError().getValue());
        }
        // Get the copyright notice which has to be loaded into every domain object we create.
        String cpyRt = response.getCopyright();

        Map<String, Stop> stopById = new HashMap<String, Stop>();
        // Unpack the 0..n Locations
        for (net.sf.nextbus.publicxmlfeed.xjcgenerated.vehiclelocation.VehicleLocation v : response.getVehicle()) {

            Geolocation lastPosition = new Geolocation(v.getLat().doubleValue(), v.getLon().doubleValue());
            // Adjust the last time by the 'seconds Since Last Report' attribute of the message.
            long lastTime = System.currentTimeMillis() - v.getSecsSinceReport() * 1000;

            VehicleLocation vehLcn = new VehicleLocation(route, v.getId(), v.getDirTag(),
                    v.isPredictable().booleanValue(), lastPosition, lastTime, v.getSpeedKmHr().doubleValue(), v.getHeading().doubleValue(), cpyRt);
            returnValue.add(vehLcn);
        }
        return returnValue;
    }

    /**
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
}
