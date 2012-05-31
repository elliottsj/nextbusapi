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

/**
 * A Simple and Easy Service Adapter to NextBus and the fastest way to get rolling.
 * <p>
 * It's simple and easy because it uses the Java.Net.UrlConnection RPC provider and thus
 * requires no addition JAR dependencies - just the vanilla JRE 5 or above.
 * </p>
 * 
 * 
 * <pre>
 *    Service svc = new SimplestServiceAdapter();
 *    Agency mbta = svc.getAgency("mbta");
 *    List<Route> mbtaRoutes = svc.getRoutes(mbta);
 *    Route route110 = Route.getRoute(mbtaRoutes, "110");
 * 
 *    List<VehicleLocation> bussesOnThe110 = svc.getVehicleLocations(route110);
 *    System.out.println("There are currently "+bussesOnThe110.size()+" busses running The 110");
 * 
 *    
 * </pre>
 * 
 * @author jrd
 */
public class SimplestServiceAdapter extends Service {
    
    public SimplestServiceAdapter() {
        super(new net.sf.nextbus.publicxmlfeed.impl.http_rpc.JavaNetRPCImpl());
    }
}
