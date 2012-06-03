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
import java.util.List;
import java.util.Collection;
import net.sf.nextbus.publicxmlfeed.domain.*;

/**
 * Adapter Interface to the NextBus Web Service using Domain classes rather than
 * wire-protocol.
 *
 * @author jrd
 */
public interface INextbusService {
    
    /**
     * Indicates the corresponding Document version from NextBus that this adapter was tested against.
     */
    public final String specVersion = "1.18";
    
    /**
     * Get all Transit Agencies currently served by NextBus.
     * @return agencies
     * @throws ServiceException Wraps all XML parse, I/O and data conversion faults detected from implementation classes.
     */
    public List<Agency> getAgencies() throws ServiceException;

    /**
     * Get Agency root object - use the getRoutes() call to dig deeper.
     * @param id agency ID
     * @return Agency
     * @throws ServiceException Wraps all XML parse, I/O and data conversion faults detected from implementation classes.
     */
    public Agency getAgency(String id) throws ServiceException;

    /**
     * Get routes covered by an agency - use getRouteConfiguration() call to dig even deeper.
     * @param agency Agency to enumerate
     * @return List of Routes covered by Agency - use RouteConfig
     * @throws ServiceException Wraps all XML parse, I/O and data conversion faults detected from implementation classes.
     */
    public List<Route> getRoutes(Agency agency) throws ServiceException;

    /**
     * Get the Static transit configuration for a Route - use getSchedule() or getPredictions() for the Dynamic snapshot view.
     * @param route Route to retrieve config
     * @return Composite object with Directions and Stops
     * @throws ServiceException Wraps all XML parse, I/O and data conversion faults detected from implementation classes.
     */
    public RouteConfiguration getRouteConfiguration(Route route) throws ServiceException;

    /**
     * Get most recent snapshot of Vehicle Locations
     * @param route Route to enumerate
     * @param deltaT Time horizon. Zero gives a 15 minute from present-time maximal view.
     * @return A List of Vehicles, their present Locations and last-time sampled.
     * @throws ServiceException Wraps all XML parse, I/O and data conversion faults detected from implementation classes.
     */
    public List<VehicleLocation> getVehicleLocations(Route route, long deltaT) throws ServiceException;

    /**
     * Get Arrival and Departure Predicted Future time for a Stop.
     * @param s Stop to enumerate.
     * @return A set of Predictions for all of the Vehicles deployed scheduled to serve the given stop.
     * @throws ServiceException Wraps all XML parse, I/O and data conversion faults detected from implementation classes.
     */
    public PredictionGroup getPredictions(Stop s) throws ServiceException;

    /**
     * Get Arrival and Departure Predicted Future time for Stops.
     * @param stops
     * @return predictions
     * @throws ServiceException Wraps all XML parse, I/O and data conversion faults detected from implementation classes.
     */
    public List<PredictionGroup> getPredictions(Collection<Stop> stops) throws ServiceException;
    
   
    /**
     * Get the nested Schedule elements for a given Route
     * @param route
     * @return schedule
     * @throws ServiceException  Wraps all XML parse, I/O and data conversion faults detected from implementation classes.
     */
    public List<Schedule> getSchedule(Route route) throws ServiceException;
     
    
    /* TODO - Implement Support for Nextbus Messages RPC once the attributes are better understood. */
}
