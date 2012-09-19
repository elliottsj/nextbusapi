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
package net.sf.nextbus.publicxmlfeed.service;
import net.sf.nextbus.publicxmlfeed.domain.Stop;
import net.sf.nextbus.publicxmlfeed.domain.PredictionGroup;
import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.VehicleLocation;
import net.sf.nextbus.publicxmlfeed.domain.RouteConfiguration;
import net.sf.nextbus.publicxmlfeed.service.ServiceException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import net.sf.nextbus.publicxmlfeed.domain.*;

/**
 * The RMI perspective of the interface.
 * 
 * @author jrd
 */
public interface INextbusServiceRemote extends Remote {
    
    public List<Agency> getAgencies() throws ServiceException, RemoteException;
    
    public Agency getAgency(String id) throws ServiceException, RemoteException;
    
    public List<Route> getRoutes(Agency agency) throws ServiceException, RemoteException;
    
    public RouteConfiguration getRouteConfiguration(Route route) throws ServiceException, RemoteException;
   
    public List<VehicleLocation> getVehicleLocations(Route route, long deltaT) throws ServiceException, RemoteException;

    public PredictionGroup getPredictions(Stop s) throws ServiceException, RemoteException;
   
    public PredictionGroup getPredictions(Route r, Stop s) throws ServiceException, RemoteException;
   
    public List<PredictionGroup> getPredictions(Route route, Collection<Stop> stops) throws ServiceException, RemoteException;
    
    public List<PredictionGroup> getPredictions(Map<Route, Stop> stops) throws ServiceException, RemoteException;
    
    public List<Schedule> getSchedule(Route route) throws ServiceException, RemoteException;
    
    /* TODO - Implement Support for Nextbus Message RPC once the attributes are netter understood */
}
