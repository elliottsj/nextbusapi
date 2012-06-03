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
 ******************************************************************************/package net.sf.nextbus.publicxmlfeed.impl;

import net.sf.nextbus.publicxmlfeed.domain.Stop;
import net.sf.nextbus.publicxmlfeed.domain.PredictionGroup;
import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.VehicleLocation;
import net.sf.nextbus.publicxmlfeed.domain.Schedule;
import net.sf.nextbus.publicxmlfeed.domain.RouteConfiguration;
import net.sf.nextbus.publicxmlfeed.service.INextbusService;
import java.util.*;
import net.sf.nextbus.publicxmlfeed.service.FatalServiceException;
import net.sf.nextbus.publicxmlfeed.service.ServiceException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;

/**
 * Flexible implementation of the NextBus Service adapter allowing selectable
 * HTTP RPC protocol provider.
 *
 * Notice how the spectrum of Communications (TCPIP) and XML Exceptions are
 * totally handled here. Also notice that all of the Communication activities
 * have been abstracted out of this class by delegating those activities to a
 * collaborating object (RPCImpl). This allows also of flexibility, for one,
 * Unit testing the Service object using sample files without having to worry
 * about the communications tier - and also - more significantly, to change
 * communications transports (i.e. java http I/O vs. commons httpclient etc)
 *
 * LogLevel.FINEST
 *
 * @author jrd
 */
public class NextbusService implements INextbusService {

    
    private static final Logger logger = Logger.getLogger(net.sf.nextbus.publicxmlfeed.impl.NextbusService.class.getName());
    private DomainFactory pojoMaker;
    private RPCImpl rpc;

    /**
     * Ctor.
     */
    public NextbusService() {
        pojoMaker = new DomainFactory();
    }

    /**
     * Really this is here to allow me to plug in a null, network-less stub.
     * This lets your implement the entire XML -> POJO -> Domain class
     * functionality before tackling Socket layer stuff.
     *
     * @param arg
     */
    public NextbusService(RPCImpl arg) {
        this();
        rpc = arg;
    }

    public List<Agency> getAgencies() throws ServiceException {
        String responseXml = "";
        // construct the wire protocol command
        RPCRequest rq = RPCRequest.newAgencyListCommand();
        logger.log(Level.FINEST, "sending RPC ", rq.getFullHttpRequest());
        try {
            // invoke the RPC
            responseXml = rpc.call(rq);
            logger.log(Level.FINEST, "got XML response ", responseXml);
            // domain factory
            return pojoMaker.getAgencies(responseXml);
        } catch (JAXBException jbe) {
            logger.log(Level.SEVERE, "while parsing response Xml " + responseXml, jbe);
            throw new FatalServiceException("RPC call <agencies>", jbe);
        }
    }

    public Agency getAgency(String id) throws ServiceException {
        List<Agency> agencies = getAgencies();
        for (Agency a : agencies) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        throw new ServiceException("Invalid call parameter. Agency " + id + " not registered with NextBus.");
    }

    public List<Route> getRoutes(Agency agency) {
        String responseXml = "";
        // construct the wire protocol command
        RPCRequest rq = RPCRequest.newRouteListCommand(agency);
        logger.log(Level.FINEST, "sending RPC ", rq.getFullHttpRequest());
        try {
            // invoke the RPC
            responseXml = rpc.call(rq);
            logger.log(Level.FINEST, "got XML response ", responseXml);
            // domain factory
            return pojoMaker.getRoutes(agency, responseXml);
        } catch (JAXBException jbe) {
            logger.log(Level.SEVERE, "while parsing response Xml " + responseXml, jbe);
            throw new FatalServiceException("RPC Call <routeList>", jbe);
        }
    }

    public RouteConfiguration getRouteConfiguration(Route parent) {
        String responseXml = "";
        // construct the wire protocol command
        RPCRequest rq = RPCRequest.newRouteConfigCommand(parent);
        logger.log(Level.FINEST, "sending RPC ", rq.getFullHttpRequest());
        try {
            // invoke the RPC
            responseXml = rpc.call(rq);
            logger.log(Level.FINEST, "got XML response ", responseXml);
            return pojoMaker.getRouteConfiguration(parent, responseXml);
        } catch (JAXBException jbe) {
            logger.log(Level.SEVERE, "while parsing response Xml " + responseXml, jbe);
            throw new FatalServiceException("RPC Call <routeConfig>", jbe);
        }
    }

    public List<VehicleLocation> getVehicleLocations(Route route, long t) {
        String responseXml = "";
        // construct the wire protocol command
        RPCRequest rq = RPCRequest.newVehicleLocationsCommand(route, t);
        logger.log(Level.FINEST, "sending RPC ", rq.getFullHttpRequest());
        try {
            // invoke the RPC
            responseXml = rpc.call(rq);
            logger.log(Level.FINEST, "got XML response ", responseXml);
            return pojoMaker.getVehicleLocations(route, responseXml);
        } catch (JAXBException jbe) {
            logger.log(Level.SEVERE, "while parsing response Xml " + responseXml, jbe);
            throw new FatalServiceException("RPC Call <vehicleLocations>", jbe);
        }
    }

    public PredictionGroup getPredictions(Stop s) throws ServiceException {
        String responseXml = "";
        // construct the wire protocol command
        RPCRequest rq = RPCRequest.newPredictionsCommand(s, false);
        logger.log(Level.FINEST, "sending RPC ", rq.getFullHttpRequest());
        List<Stop> stops = new ArrayList<Stop>();
        stops.add(s);
        try {
            // invoke the RPC
            responseXml = rpc.call(rq);
            logger.log(Level.FINEST, "got XML response ", responseXml);
            List<PredictionGroup> pg = pojoMaker.getPredictions(stops, responseXml);
            return pg.get(0);
        } catch (JAXBException jbe) {
            logger.log(Level.SEVERE, "while parsing response Xml " + responseXml, jbe);
            throw new FatalServiceException("RPC Call <predictions>", jbe);
        }
    }

    public List<PredictionGroup> getPredictions(Collection<Stop> s) throws ServiceException {
        String responseXml = "";
        // construct the wire protocol command
        RPCRequest rq = RPCRequest.newPredictionsCommand(s, false);
        logger.log(Level.FINEST, "sending RPC ", rq.getFullHttpRequest());
        try {
            // invoke the RPC
            responseXml = rpc.call(rq);
            logger.log(Level.FINEST, "got XML response ", responseXml);
            return pojoMaker.getPredictions(s, responseXml);
        } catch (JAXBException jbe) {
            logger.log(Level.SEVERE, "while parsing response Xml " + responseXml, jbe);
            throw new FatalServiceException("RPC Call <multiStopPredictions>", jbe);
        }
    }
    
    public List<Schedule> getSchedule(Route route) throws ServiceException {
         String responseXml = "";
        // construct the wire protocol command
        RPCRequest rq = RPCRequest.newScheduleReqCommand(route);
        logger.log(Level.FINEST, "sending RPC ", rq.getFullHttpRequest());
        try {
           // invoke the RPC
            responseXml = rpc.call(rq);
            logger.log(Level.FINEST, "got XML response ", responseXml); 
            return pojoMaker.getSchedule(route, responseXml);
        } catch (JAXBException jbe) {
            logger.log(Level.SEVERE, "while parsing response Xml " + responseXml, jbe);
            throw new FatalServiceException("RPC Call <schedules>", jbe);
        }
    }
}