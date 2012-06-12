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
import com.google.gson.Gson;
import net.sf.nextbus.proxy.ejb.NextbusFeedServiceLocal;
import net.sf.nextbus.publicxmlfeed.domain.*;
import net.sf.nextbus.publicxmlfeed.service.*;

/**
 * Adapts the NextbusService::getAgencies method to a JSON compatible HTTP Get Request
 * @author jrd
 */
@WebServlet(name = "GetAgenciesServlet", urlPatterns = {"/getAgencies"})
public class GetAgenciesServlet extends HttpServlet {

    private @EJB NextbusFeedServiceLocal nextbus;
    private Gson gson;

    public GetAgenciesServlet() {
        this.gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       
        try {
            List<Agency> agencies = nextbus.getAgencies();
            String result = gson.toJson(agencies);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");
            resp.getWriter().print(result);
        } catch (net.sf.nextbus.publicxmlfeed.service.ServiceException nbe ) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            resp.getWriter().append(nbe.toString()+":"+nbe.getMessage());
            log("Nextbus adapter dropped exception while servicing JSON call...", nbe);
        } catch (Exception anyOther) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().append(anyOther.toString()+":"+anyOther.getMessage());
            log("While trying to serve JSON call...", anyOther);
        } finally {
            resp.flushBuffer();
        }
    }
   
}
