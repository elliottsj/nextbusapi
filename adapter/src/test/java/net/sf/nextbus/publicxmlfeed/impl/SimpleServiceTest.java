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
import net.sf.nextbus.publicxmlfeed.domain.VehicleLocation;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.RouteConfiguration;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import net.sf.nextbus.publicxmlfeed.impl.rmiproxy.RMIClient;
import net.sf.nextbus.publicxmlfeed.service.INextbusService;
import org.junit.Before;

/**
 *
 * @author jrd
 */
public class SimpleServiceTest {
    
    Random random = new Random(System.currentTimeMillis());
    INextbusService svc;
    
    /** Test Harness for RMI */
    public INextbusService remoteBinding() {
        RMIClient rmicli = new RMIClient("192.168.11.2");
        return rmicli.getService();
    }
    /** Normal Ordinary Test Harness */
    public INextbusService localBinding() {
        return new SimplestNextbusServiceAdapter();
    }
    
    
    @Before
    public void setup() throws Exception {
         //svc = remoteBinding();
         svc=localBinding();
    }
    
    //@Test
    public void testAgencyList() throws Exception {
     
        List<Agency> agencies = svc.getAgencies();
        Assert.assertTrue(agencies.size() != 0);
        for (Agency a : agencies) {
            Assert.assertNotNull(a.getTag());
            Assert.assertNotNull(a.getTitle());
            Assert.assertNotNull(a.getShortTitle());
            Assert.assertNotNull(a.getRegionTitle());
            System.out.println(a);
        }
    }
    
    @Test
    public void testWalkMBTARoutes110and111() throws Exception {
        long f = -1;
    }
    
    
    @Test
    public void testWalkMBTA() throws Exception {
        
        
        Agency mbta = svc.getAgency("mbta");
        Assert.assertNotNull(mbta);
        
        List<Route> routes = svc.getRoutes(mbta);
        Assert.assertNotNull(routes);
        Assert.assertTrue(routes.size() != 0);
        for (Route r : routes) {
            System.out.println(r);
        }
        System.out.println("Obtained "+routes.size()+" Routes");
     
        // Randomly pick a route out and pull down its configuration
        int pick = random.nextInt(routes.size());
        
        Route randomRoute = routes.get(pick);
        
        RouteConfiguration rc = svc.getRouteConfiguration(randomRoute);
        Assert.assertNotNull(rc);
        Assert.assertNotNull(rc.getDirections());
        Assert.assertNotNull(rc.getPaths());
        Assert.assertNotNull(rc.getRoute());
        Assert.assertNotNull(rc.getStops());
       
        
        System.out.println(rc);
        
        System.out.println("Getting locations for route "+randomRoute.getTag());
       List<VehicleLocation> vehicleLocns = svc.getVehicleLocations(randomRoute, 0);
       //Assert.assertTrue(vehicleLocns.size() > 0);
       for (VehicleLocation v : vehicleLocns) {
           System.out.println("  vehicle: "+v);
       }
        
    }
}
