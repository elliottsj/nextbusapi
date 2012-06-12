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
package net.sf.nextbus.servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import javax.ejb.EJB;
import flexjson.JSONSerializer;
import flexjson.JSONDeserializer;
import java.util.Arrays;
import net.sf.nextbus.proxy.ejb.NextbusFeedServiceLocal;
import net.sf.nextbus.publicxmlfeed.domain.*;
import net.sf.nextbus.publicxmlfeed.service.*;

/**
 * The Raw Nextbus API exported via JSON for JQuery and DOJO Savvy Web Developers. 
 * HTTP Post Parameters: <br>
 * method: <br>
 * callParams: <br>
 * <br>
 * HTTP Response:
 * Status Code<br>
 * JSON returned as the response body.<br>
 * <p>
 * This is not the best starting point for building a serious Web or thin-client application as 
 * the organization of data is still too "raw" for an end-user use case scenarios. Nonetheless, it's
 * a good example of how to write JSON Servlets in an EJB world. In Spring, you could even use a VIEW
 * resolver to translate the the "Model" elements (i.e. NextBus domain classes) to JSON. 
 * </p>
 * @author jrd
 */
@WebServlet(name = "NextbusRawServlet", urlPatterns = {"/nextbus-rawapi"})
public class NextbusRawJSONServlet extends HttpServlet {
    
    private @EJB NextbusFeedServiceLocal nextbus;
    private JSONSerializer java2json;
    private JSONDeserializer json2java;

    /** The enumeration of allowed Command verbs this JSON Servlet will serve */
    private String [] methods = { "getAgency", "getRoutes", "getRouteConfiguration", "getVehicleLocations", "getPredictions", "getSchedule" };
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        // Get the command verb to execute.....
        String method = req.getParameter("method");
        if (Arrays.asList(methods).contains(method) == false) {
            // Exception - no such method!
        }
        String param = req.getParameter("callParams");
        Object r = null;
       
        // Dispatch the Command verb to the appropriate Nextbus Service Method - doing JSON action as needed.
        
        if (method.equals("getAgency")) {
            r = nextbus.getAgency(param);
        }
        
        if (method.equals("getRoutes")) {
            Agency a = (Agency) json2java.deserialize(param, net.sf.nextbus.publicxmlfeed.domain.Agency.class);
            r = nextbus.getRoutes(a);
        }
        
        if (method.equals("getRouteConfiguration")) {
            // TODO implement this method
        }
        if (method.equals("getVehicleLocations")) {
            // TODO implement this method
        }
        if (method.equals("getPredictions")) {
            // TODO implement this method
        }
        if (method.equals("getSchedule")) {
            // TODO implement this method
        }
        
        String json = java2json.deepSerialize(r);
        jsonResponse(json, resp);
        
    }
    
    /**
     * Sets up the HTTP Response to transmit JSON back to the requestor.
     * @param json the JSON response string to send
     * @param resp the Servlet container injected HTTP responder.
     * @throws IOException 
     */
    private void jsonResponse(String json, HttpServletResponse resp) throws IOException {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");
            resp.getWriter().print(json);
            resp.flushBuffer();
    }
    
    
}
