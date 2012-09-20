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

import net.sf.nextbus.publicxmlfeed.domain.Stop;
import net.sf.nextbus.publicxmlfeed.domain.PredictionGroup;
import net.sf.nextbus.publicxmlfeed.domain.Prediction;
import net.sf.nextbus.publicxmlfeed.domain.Path;
import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.VehicleLocation;
import net.sf.nextbus.publicxmlfeed.domain.Schedule;
import net.sf.nextbus.publicxmlfeed.domain.DailySchedule;
import net.sf.nextbus.publicxmlfeed.domain.RouteConfiguration;
import net.sf.nextbus.publicxmlfeed.domain.Direction;
import net.sf.nextbus.publicxmlfeed.domain.Geolocation;
import net.sf.nextbus.publicxmlfeed.service.TransientServiceException;
import net.sf.nextbus.publicxmlfeed.service.ServiceConfigurationException;

import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.nextbus.publicxmlfeed.service.ValueConversionException;

/**
 * Converts the JAXB wire protocol (XML) object stream into Java Domain objects
 * (POJOs).
 *
 * @author jrd
 */
public class DomainFactory {

    private static final Logger logger = Logger.getLogger(DomainFactory.class.getName());
    private net.sf.nextbus.publicxmlfeed.impl.jaxb.RouteConfigServiceBeanFactory routeCfgSvc;
    private net.sf.nextbus.publicxmlfeed.impl.jaxb.RouteListServiceBeanFactory routeListSvc;
    private net.sf.nextbus.publicxmlfeed.impl.jaxb.VehicleLocationServiceBeanFactory vehicleLocnSvc;
    private net.sf.nextbus.publicxmlfeed.impl.jaxb.PredictionServiceBeanFactory predictionSvc;
    private net.sf.nextbus.publicxmlfeed.impl.jaxb.AgencyListServiceBeanFactory agcySvc;
    private net.sf.nextbus.publicxmlfeed.impl.jaxb.ServiceMessagesServiceBeanFactory msgsSvc;
    private net.sf.nextbus.publicxmlfeed.impl.jaxb.ScheduleServiceBeanFactory schedSvc;

    /**
     * Ctor
     *
     * @exception ServiceConfigurationException wraps JAXB and SAX exceptions
     * indicating a misconfiguration of the JAXB environment.
     */
    public DomainFactory() {


        try {
            // start all the JAXB factories...
            agcySvc = new net.sf.nextbus.publicxmlfeed.impl.jaxb.AgencyListServiceBeanFactory();
            routeListSvc = new net.sf.nextbus.publicxmlfeed.impl.jaxb.RouteListServiceBeanFactory();
            routeCfgSvc = new net.sf.nextbus.publicxmlfeed.impl.jaxb.RouteConfigServiceBeanFactory();
            vehicleLocnSvc = new net.sf.nextbus.publicxmlfeed.impl.jaxb.VehicleLocationServiceBeanFactory();
            predictionSvc = new net.sf.nextbus.publicxmlfeed.impl.jaxb.PredictionServiceBeanFactory();
            schedSvc = new net.sf.nextbus.publicxmlfeed.impl.jaxb.ScheduleServiceBeanFactory();
            msgsSvc = new net.sf.nextbus.publicxmlfeed.impl.jaxb.ServiceMessagesServiceBeanFactory();
        } catch (SAXException sax) {
            logger.log(Level.SEVERE, "JAXB Config problem during instantiation of XML Parsers in DomainFactory", sax);
            throw new ServiceConfigurationException("JAXB Config problem during instantiation of XML Parsers in DomainFactory", sax);
        } catch (JAXBException jaxb) {
            logger.log(Level.SEVERE, "JAXB Config problem during instantiation of XML Parsers in DomainFactory", jaxb);
            throw new ServiceConfigurationException("JAXB Config problem during instantiation of XML Parsers in DomainFactory", jaxb);
        }
    }

    /**
     *
     * @param xml agencyList XML
     * @return Agency POJOs
     * @throws JAXBException Propagates any JAXB Exceptions while attempting to
     * parse XML.
     */
    public List<Agency> getAgencies(String xml) throws JAXBException {
        List<Agency> returnVal = new ArrayList<Agency>();
        net.sf.nextbus.publicxmlfeed.xjcgenerated.agencylist.Body response = agcySvc.parse(xml);
        // If the remote web service has cast a defined exception
        if (response.getError() != null) {
            throw new TransientServiceException(response.getError().isShouldRetry(), response.getError().getValue());
        }
        // Get the copyright notice which has to be loaded into every domain object we create.
        String cpyRt = response.getCopyright();

        for (net.sf.nextbus.publicxmlfeed.xjcgenerated.agencylist.Agency a : response.getAgency()) {
            returnVal.add(new Agency(a.getTag(), a.getTitle(), a.getShortTitle(), a.getRegionTitle(), cpyRt));
        }
        return returnVal;
    }

    /**
     *
     * @param rootAgency Enclosing Agency that owns the constructed Routes
     * @param xml routeList xml
     * @return Route POJOs
     * @throws JAXBException Propagates any JAXB Exceptions while attempting to
     * parse XML.
     */
    public List<Route> getRoutes(Agency rootAgency, String xml) throws JAXBException {
        List<Route> returnVal = new ArrayList<Route>();
        net.sf.nextbus.publicxmlfeed.xjcgenerated.routelist.Body response = routeListSvc.parse(xml);
        // If the remote web service has cast a defined exception
        if (response.getError() != null) {
            throw new TransientServiceException(response.getError().isShouldRetry(), response.getError().getValue());
        }
        // Get the copyright notice which has to be loaded into every domain object we create.
        String copyRt = response.getCopyright();

        // 
        // convert from wire representation to a native implementation
        // 
        for (net.sf.nextbus.publicxmlfeed.xjcgenerated.routelist.Route r : response.getRoute()) {
            // Short title is NOT available from this API method... Actually, that should be filed as a bug with Nextbus
            returnVal.add(new Route(rootAgency, r.getTag(), r.getTitle(), "", copyRt));
        }
        return returnVal;
    }

    /**
     *
     * @param parent Route (and nested Agency) that owns the constructed target
     * object
     * @param xml routeConfig xml
     * @return RouteConfiguration composite POJO
     * @throws JAXBException Propagates any JAXB Exceptions while attempting to
     * parse XML.
     */
    public RouteConfiguration getRouteConfiguration(Route parent, String xml) throws JAXBException {
        List<Stop> stops = new ArrayList<Stop>();
        List<Direction> directions = new ArrayList<Direction>();
        List<Path> paths = new ArrayList<Path>();

        // parse the response XML
        net.sf.nextbus.publicxmlfeed.xjcgenerated.routeconfig.Body response = routeCfgSvc.parse(xml);
        // If the remote web service has cast a defined exception
        if (response.getError() != null) {
            throw new TransientServiceException(response.getError().isShouldRetry(), response.getError().getValue());
        }

        // Get the copyright notice which has to be loaded into every domain object we create.
        String cpyRt = response.getCopyright();
        Map<String, Stop> stopById = new HashMap<String, Stop>();
        // Unpack the 0..n Stops
        for (net.sf.nextbus.publicxmlfeed.xjcgenerated.routeconfig.Stop s : response.getRoute().getStop()) {
            Geolocation gps = new Geolocation(s.getLat().doubleValue(), s.getLon().doubleValue());
            Stop stop = new Stop(parent.getAgency(), s.getStopId(), s.getTag(), s.getTitle(), s.getShortTitle(), gps, cpyRt);
            stops.add(stop);
            stopById.put(s.getStopId(), stop);
        }

        // Unpack the 0..n Paths
        int pathId = 0;
        for (net.sf.nextbus.publicxmlfeed.xjcgenerated.routeconfig.Path p : response.getRoute().getPath()) {
            List<Geolocation> points = new ArrayList<Geolocation>();
            for (net.sf.nextbus.publicxmlfeed.xjcgenerated.routeconfig.Point pt : p.getPoint()) {
                points.add(new Geolocation(pt.getLat().doubleValue(), pt.getLon().doubleValue()));
            }
            paths.add(new Path(parent, Integer.toString(pathId), points));
            pathId++;
        }

        // Unpack the 0..n Directions - reuse the map of stops we created above to build a full object tree
        for (net.sf.nextbus.publicxmlfeed.xjcgenerated.routeconfig.Direction d : response.getRoute().getDirection()) {
            List<Stop> directions4ThisStop = new ArrayList<Stop>();
            for (net.sf.nextbus.publicxmlfeed.xjcgenerated.routeconfig.Direction.Stop ds : d.getStop()) {
                directions4ThisStop.add(stopById.get(ds.getTag()));
            }
            List<Stop> unmod = Collections.unmodifiableList(directions4ThisStop);
            Direction direction = new Direction(parent, d.getTag(), d.getTitle(), d.getName(), unmod, cpyRt);
            directions.add(direction);
        }

        response.getRoute().getOppositeColor();
        response.getRoute().getColor();
        RouteConfiguration.ServiceArea sa = new RouteConfiguration.ServiceArea(
                response.getRoute().getLatMin().doubleValue(),
                response.getRoute().getLatMax().doubleValue(),
                response.getRoute().getLonMin().doubleValue(),
                response.getRoute().getLonMax().doubleValue());
        RouteConfiguration.UIColor color = new RouteConfiguration.UIColor(response.getRoute().getColor());
        RouteConfiguration.UIColor oppositeColor = new RouteConfiguration.UIColor(response.getRoute().getOppositeColor());

        // Render the lists Immutable construct the final domain object.
        return new RouteConfiguration(parent,
                Collections.unmodifiableList(stops),
                Collections.unmodifiableList(directions),
                Collections.unmodifiableList(paths),
                sa,
                oppositeColor,
                color,
                cpyRt);

    }

    /**
     *
     * @param route Route (and nested Agency) that owns the constructed domain
     * objects.
     * @param xml vehicleLocations xml
     * @return VehicleLocation POJOs
     * @throws JAXBException Propagates any JAXB Exceptions while attempting to
     * parse XML.
     */
    public List<VehicleLocation> getVehicleLocations(Route route, String xml) throws JAXBException {
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
     *
     * @param stops
     * @param xml predictions XML
     * @return A PredictionGroup aggregator object and multiple Prediction POJOs
     * @throws JAXBException Propagates any JAXB Exceptions while attempting to
     * parse XML.
     */
    public List<PredictionGroup> getPredictions(Collection<Stop> stops, String xml) throws JAXBException {

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
     * @param route route
     * @param xml XML
     * @throws JAXBException Propagates any JAXB Exceptions while attempting to
     * parse XML.
     * @throws ValueConversionException Any additional data conversions
     * encountered AFTER XML Parsing.
     */
    public List<Schedule> getSchedule(Route route, String xml) throws JAXBException {
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
