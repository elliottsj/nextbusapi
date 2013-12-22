/**
 * *****************************************************************************
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
 * Usage of the NextBus Web Service and its data is subject to separate Terms
 * and Conditions of Use (License) available at:
 *
 * http://www.nextbus.com/xmlFeedDocs/NextBusXMLFeed.pdf
 *
 *
 * NextBus® is a registered trademark of Webtech Wireless Inc.
 *
 *****************************************************************************
 */
package net.sf.nextbus.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import javax.ejb.EJB;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import net.sf.nextbus.proxy.ejb.NextbusFeedServiceLocal;
import net.sf.nextbus.publicxmlfeed.domain.*;
import net.sf.nextbus.publicxmlfeed.service.*;

/**
 * The Raw Nextbus API exported via JSON for JQuery and DOJO Savvy Web
 * Developers. HTTP Post Parameters: <br> method: <br> callParams: <br> <br>
 * HTTP Response: Status Code<br> JSON returned as the response body.<br> <p>
 * This is not the best starting point for building a serious Web or thin-client
 * application as the organization of data is still too "raw" for an end-user
 * use case scenarios. Nonetheless, it's a good example of how to write JSON
 * Servlets in an EJB world. In Spring, you could even use a VIEW resolver to
 * translate the the "Model" elements (i.e. NextBus domain classes) to JSON.
 * </p>
 *
 * @author jrd
 */
@WebServlet(name = "NextbusRawServlet", urlPatterns = {"/nextbus-rawapi"})
public class NextbusRawJSONServlet extends HttpServlet {

    private @EJB
    NextbusFeedServiceLocal nextbus;
    private Gson gson;
    /**
     * The enumeration of allowed Command verbs this JSON Servlet will serve
     */
    private String[] methods = {"getAgency", "getRoutes", "getRouteConfiguration", "getVehicleLocations", "getPredictions", "getSchedule"};

    public NextbusRawJSONServlet() {
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get the command verb to execute.....
        String method = req.getParameter("method");

        if (Arrays.asList(methods).contains(method) == false) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text");
            resp.setCharacterEncoding("utf-8");
            resp.getWriter().print("Invalid value for post parameter 'method' = "+method);
            return;
        }
        
        String param = req.getParameter("callParams");
        Object r = null;

        // Dispatch the Command verb to the appropriate Nextbus Service Method - doing JSON action as needed.
        try {
            if (method.equals("getAgency")) {
                Map m = gson.fromJson(param, Map.class);
                r = nextbus.getAgency((String) m.get("agency"));
            }

            if (method.equals("getRoutes")) {
                Agency a = gson.fromJson(param, Agency.class);
                r = nextbus.getRoutes(a);
            }

            if (method.equals("getRouteConfiguration")) {
                Route route = gson.fromJson(param, Route.class);
                r = nextbus.getRouteConfiguration(route);
            }
            if (method.equals("getVehicleLocations")) {
                Route route = gson.fromJson(param, Route.class);
                r = nextbus.getVehicleLocations(route, 0);

            }
            if (method.equals("getPredictions")) {
                List<Stop> stops = new ArrayList<Stop>();
                stops.add(gson.fromJson(param, Stop.class));
                r = nextbus.getPredictions(stops);
            }
            if (method.equals("getSchedule")) {
                Route route = gson.fromJson(param, Route.class);
                r = nextbus.getSchedule(route);
            }

            // Convert the Domain object 'r' to a JSON String... 
            // Set the HTTP Response code and encapsulation and return...
            String json = gson.toJson(r);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");
            resp.getWriter().print(json);
            resp.flushBuffer();
        } catch (ServiceException nbse) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");
            resp.getWriter().print(nbse.getClass().getName() + ":" + nbse.getMessage());
            log("Fault while invoking the NextBus Adapter",nbse);
        } finally {
            resp.flushBuffer();
        }
    }
}
