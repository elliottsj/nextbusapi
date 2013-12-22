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
package net.sf.nextbus.proxy.ejb;

import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.annotation.PostConstruct;
import net.sf.nextbus.publicxmlfeed.domain.*;
import net.sf.nextbus.publicxmlfeed.service.INextbusService;
import net.sf.nextbus.publicxmlfeed.service.ServiceException;

/**
 * A Stateless Session Bean wraps the Generic Nextbus Adapter so we can easily write Servlets that take Injected Nextbus Service.
 * <p>
 * If this were Spring, you'd use a similar technique called 'Auto-wire' to inject the Service proxy into your Servlet to permit
 * the UI actions to tickle your business tier... Well, in the EJB world it's the exact same idea. The Stateless beans is the autowired
 * resource. Give it a EJB Local interface and your UI tier can easily get direct access to the NextBus adapter.
 * </p>
 * <p>
 * In the Adapter project, I also built support for RMI ; So that the thin-client things like Phones or Kiosks can eat Nextbus domain
 * classes. Since we are now working in an EJB Container, we can continue to support thin-client architectures by simply adding a 
 * Remote EJB interface to this Stateless Session Bean as well.   Now, your thin-client gadgets can eat Serialized domain objects over RMI
 * but this time, they will lookup the RMI Object using a JNDI lookup rather than contacting an RMID on port 1099.
 * </p>
 * <p>
 * Did I have to write a deployment description?  No. Did I have to write all this glue code by hand?  No.   I used the IDE's
 * Code Helpers to do all this. How?   First make sure your EJB as at least on Interface that extends NextbusFeedService.
 * Second, add a private or protected INextbusService variable.   Now the IDE has a way to infer a connection between
 * Overriden methods from the Interface, and Class methods from the member attribute.   Next, Right Click - > Insert Code -> Delegate.
 * BAM!  All this code is written for you by the IDE.
 * </p?
 * 
 * @author jrd
 */
@Stateless
public class NextbusFeedService implements NextbusFeedServiceRemote, NextbusFeedServiceLocal {

    @PostConstruct
    public void init() {
        adapter = new net.sf.nextbus.publicxmlfeed.impl.SimplestNextbusServiceAdapter();
    }
    
    /** The NextBus XML adapter */
    private INextbusService adapter;

     // IDE Trick - add this property, then Insert Code -> Delegate Method
    
    @Override
    public List<VehicleLocation> getVehicleLocations(Route route, long deltaT) throws ServiceException {
        return adapter.getVehicleLocations(route, deltaT);
    }
    @Override
    public List<Schedule> getSchedule(Route route) throws ServiceException {
        return adapter.getSchedule(route);
    }
    @Override
    public List<Route> getRoutes(Agency agency) throws ServiceException {
        return adapter.getRoutes(agency);
    }
    @Override
    public RouteConfiguration getRouteConfiguration(Route route) throws ServiceException {
        return adapter.getRouteConfiguration(route);
    }
    @Override
    public List<PredictionGroup> getPredictions(Collection<Stop> stops) throws ServiceException {
        return adapter.getPredictions(stops);
    }
    @Override
    public PredictionGroup getPredictions(Stop s) throws ServiceException {
        return adapter.getPredictions(s);
    }
    @Override
    public Agency getAgency(String id) throws ServiceException {
        return adapter.getAgency(id);
    }
    @Override
    public List<Agency> getAgencies() throws ServiceException {
        return adapter.getAgencies();
    }
}
