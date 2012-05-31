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

import net.sf.nextbus.publicxmlfeed.impl.RPCImpl;
import net.sf.nextbus.publicxmlfeed.impl.RPCRequest;

/**
 * A Test Harness to simulate a Network response using a local fileset of Canned XML Responses.
 * 
 * This test harness permits a test-driven development activity to reverse engineer
 * the XSD Schema for the NextBus Web API.
 * 
 * @author jrd
 */
public class NullRpcImpl implements RPCImpl {

    public void setRoutelistResponse(String arg) { rlist = arg; }
    public void setRouteConfigResponse(String arg) { rconfig = arg; }
    
    private String rlist, rconfig;
    
    public String call(RPCRequest request) {
       
        if (request.parameters.get("command").equals("agencyList")) {
            return net.sf.nextbus.publicxmlfeed.TestUtil.loadXMLDocumentFromClasspath("positive-response-cases/agencylist/testcase.xml");
        }
        if (request.parameters.get("command").equals("routeConfig")) {
            return net.sf.nextbus.publicxmlfeed.TestUtil.loadXMLDocumentFromClasspath("positive-response-cases/routeconfig/testcase.xml");
        }
        if (request.parameters.get("command").equals("routeList")) {
            return net.sf.nextbus.publicxmlfeed.TestUtil.loadXMLDocumentFromClasspath("positive-response-cases/routelist/testcase.xml");
        }
        if (request.parameters.get("command").equals("vehicleLocation")) {
            return net.sf.nextbus.publicxmlfeed.TestUtil.loadXMLDocumentFromClasspath("positive-response-cases/vehiclelocation/testcase.xml");
        }
        if (request.parameters.get("command").equals("predictions")) {
            return net.sf.nextbus.publicxmlfeed.TestUtil.loadXMLDocumentFromClasspath("positive-response-cases/prediction/testcase.xml");
        }
        if (request.parameters.get("command").equals("predictionsForMultiStops")) {
             return net.sf.nextbus.publicxmlfeed.TestUtil.loadXMLDocumentFromClasspath("positive-response-cases/prediction-multistop/testcase.xml");
        }
        if (request.parameters.get("command").equals("predictionsForMultiStops")) {
             return net.sf.nextbus.publicxmlfeed.TestUtil.loadXMLDocumentFromClasspath("positive-response-cases/prediction-multistop/testcase.xml");
        }
         if (request.parameters.get("command").equals("schedule")) {
             return net.sf.nextbus.publicxmlfeed.TestUtil.loadXMLDocumentFromClasspath("positive-response-cases/schedule/testcase.xml");
        }
       
       throw new RuntimeException("Invalid RPC command "+request.parameters.get("command"));
        
    }

    public void activate() {}

    public void passivate() {}
      
}
