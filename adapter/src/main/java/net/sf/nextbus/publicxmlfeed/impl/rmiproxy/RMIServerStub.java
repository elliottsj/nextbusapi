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
package net.sf.nextbus.publicxmlfeed.impl.rmiproxy;

import net.sf.nextbus.publicxmlfeed.domain.Stop;
import net.sf.nextbus.publicxmlfeed.domain.PredictionGroup;
import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.VehicleLocation;
import net.sf.nextbus.publicxmlfeed.domain.RouteConfiguration;
import net.sf.nextbus.publicxmlfeed.service.INextbusServiceRemote;
import net.sf.nextbus.publicxmlfeed.service.INextbusService;
import net.sf.nextbus.publicxmlfeed.service.ServiceException;
import net.sf.nextbus.publicxmlfeed.impl.SimplestNextbusServiceAdapter;
import java.util.List;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.Map;
import net.sf.nextbus.publicxmlfeed.domain.*;

/**
 * RMI Remote Server Stub
 * @author jrd
 */
public class RMIServerStub extends UnicastRemoteObject implements INextbusServiceRemote {

    private static final Logger logger = Logger.getLogger(RMIServerStub.class.getName());
    private INextbusService svc;

    public RMIServerStub() throws RemoteException {
        super();
        svc = new SimplestNextbusServiceAdapter();
        logger.log(Level.INFO, "The RMI adapter has started...");
    }

    public List<Agency> getAgencies() throws ServiceException, RemoteException {
        logger.info("getAgencies()");
        return svc.getAgencies();
    }

    public Agency getAgency(String id) throws ServiceException, RemoteException {
        logger.info("getAgency()");
        return svc.getAgency(id);
    }

    public PredictionGroup getPredictions(Stop s) throws ServiceException, RemoteException {
        logger.info("getPredictions()");
        return svc.getPredictions(s);
    }

    public List<PredictionGroup> getPredictions(Route route, Collection<Stop> stops) throws ServiceException, RemoteException {
        logger.info("getPredictions()");
        return svc.getPredictions(route, stops);
    }

    public PredictionGroup getPredictions(Route r, Stop s) throws ServiceException {
        logger.info("getPredictions()");
       return svc.getPredictions(r, s);
    }

    public List<PredictionGroup> getPredictions(Map<Route, Stop> stops) throws ServiceException {
        logger.info("getPredictions()");
        return svc.getPredictions(stops);
    }

    
    public RouteConfiguration getRouteConfiguration(Route route) throws ServiceException, RemoteException {
        logger.info("getRouteConfiguration()");
        return svc.getRouteConfiguration(route);
    }

    public List<Route> getRoutes(Agency agency) throws ServiceException, RemoteException {
        logger.info("getRoutes()");
        return svc.getRoutes(agency);
    }

    public List<VehicleLocation> getVehicleLocations(Route route, long deltaT) throws ServiceException, RemoteException {
        logger.info("getVehicleLocations()");
        return svc.getVehicleLocations(route, deltaT);
    }
    
    public List<Schedule> getSchedule(Route route) throws ServiceException, RemoteException {
        logger.info("getSchedule()");
        return svc.getSchedule(route);
        
    }
}
