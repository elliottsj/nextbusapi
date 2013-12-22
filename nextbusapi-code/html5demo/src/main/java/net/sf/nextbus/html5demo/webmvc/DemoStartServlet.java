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

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.nextbus.html5demo.SessionProxy;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jrd
 */
@WebServlet(name = "DemoStartServlet", urlPatterns = {"/entry"})
public class DemoStartServlet extends HttpServlet {

    @Inject
    private SessionProxy sessionProxy;
    
   
    
    
    private static final Logger log = LoggerFactory.getLogger(DemoStartServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            String sessId = request.getSession().getId();
            sessionProxy.init(sessId, request.getUserPrincipal());
            // create a new Session Proxy and associate
            log.info("created new web session {} for a client {} ", new Object[]{sessId, request.getHeader("User-Agent")});
        request.getRequestDispatcher("location").forward(request, response);
    }

    /**
     * Nuke operation - allows the web client to request destruction of the session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getParameter("action") != null && request.getParameter("action").equalsIgnoreCase("nuke")) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                sessionProxy.close();
                String sessionId = session.getId();
                session.invalidate();
                log.info("invalidated web container session.");
                response.setContentType("text/html");
                response.setCharacterEncoding("utf-8");
                response.getWriter().print("<html><body>Nuked web session ");
                response.getWriter().print(sessionId);
                response.getWriter().print("</body><html>");
                response.setStatus(java.net.HttpURLConnection.HTTP_OK);
            } else {
                log.info("No session to nuke!");
            }
        }
    }
}
