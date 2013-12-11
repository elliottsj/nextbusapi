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
package net.sf.nextbus.html5demo.restful;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.nextbus.html5demo.SessionProxy;
import net.sf.nextbus.html5demo.service.ClientEventStream.EventEntry;
import net.sf.nextbus.html5demo.service.ClientEventStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
 * http://dev.w3.org/html5/eventsource/
 * @author jrd
 *
 */
@WebServlet(name = "EventStreamServlet", urlPatterns = {"/eventstream"})
public class EventStreamServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(EventStreamServlet.class);
    
    @Inject
    SessionProxy sessionProxy;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Reject the request if it is not for an event stream
        if (request.getHeader("Accept").equalsIgnoreCase("text/event-stream") == false) {
            response.getWriter().println("HTML 5 Server Sent Events - expected text/event-stream");
            response.setStatus(java.net.HttpURLConnection.HTTP_NO_CONTENT);
            return;
        }
        
        // on connection open, the client may send the Last Event Id it received..
        String lastEventId = request.getHeader("Last-Event-ID");
        // setup the standard response
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/event-stream");
        response.setHeader("connection", "keep-alive");
        response.getWriter().println();
        // if there is no web-container session associated with the call, terminate connection.
        // You must send an HTTP 204 to the client to prevent it from reconnect/retry cycles.
        if (request.getSession(false) == null) {
            response.setStatus(java.net.HttpURLConnection.HTTP_NO_CONTENT);
            log.error("Sending HTTP 204. No Web Session present ; cannot continue.");
            return;
        }
        ClientEventStream evStream = sessionProxy.getEventStreamQueue();
        log.info("got session bound feedstream {} ", new Object [] { evStream });

        // Identify the event service type and retry interval to the client.
        //response.setStatus(java.net.HttpURLConnection.HTTP_OK);
        //response.getWriter().println(": gps event source");
        //response.getWriter().println("retry: 5000");
        //response.getWriter().println();
        //response.flushBuffer();
        log.debug("Event Service returned open acknowledgement header.");

        // Yes - it's an infinite loop!
        try {
            while (true) {
                try {
                    EventEntry e = evStream.getNextEventToSend();
                    response.getWriter().println(evStream.toJSON(e));
                    response.getWriter().println(); // Event Stream REQUIRES newline between msg blocks.
                    evStream.acknowledgeSent(e);
                    log.debug("sent event");
                } catch (InterruptedException waiterr) {
                    log.warn("thread wait in getNextEvent() was interrupted. Continuing. ");
                } finally {
                    response.setStatus(java.net.HttpURLConnection.HTTP_OK);
                    response.flushBuffer();
                }
            } //end-forever-while
        } catch (Exception any) {
            log.error("Exception during network I/O. terminating connection. ");
            response.setStatus(java.net.HttpURLConnection.HTTP_INTERNAL_ERROR);
            response.flushBuffer();
        }
    }

    @Override
    public String getServletInfo() {
        return "HTML 5 Server Side Event Servlet";
    }
}
