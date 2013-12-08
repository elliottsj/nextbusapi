/*******************************************************************************
 * Copyright (C) 2013 by James R. Doyle
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
package net.sf.nextbus.html5demo.webmvc;

import net.sf.nextbus.html5demo.SessionProxy;
import java.io.IOException;
import java.io.PrintWriter;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jrd
 */
@WebServlet(name = "LocationControlServlet", urlPatterns = {"/location"})
public class LocationControlServlet extends HttpServlet {

    @Inject
    SessionProxy sessionProxy;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("latitude", sessionProxy.getLatitude());
        request.setAttribute("longitude", sessionProxy.getLongitude());
        request.setAttribute("range", sessionProxy.getRange());
        request.setAttribute("units", sessionProxy.getUnits());
        request.getRequestDispatcher("/location/view").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Double latitude = Double.valueOf(request.getParameter("latitude"));
            Double longitude = Double.valueOf(request.getParameter("longitude"));
            Double range = Double.valueOf(request.getParameter("range"));
            String units = request.getParameter("units");
            sessionProxy.setNewSearchRadius(latitude, longitude, range, units);
        } catch (NumberFormatException nfe) {
            super.log("error setting search params",nfe);
        }
        request.getRequestDispatcher("/eventstream/view").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
