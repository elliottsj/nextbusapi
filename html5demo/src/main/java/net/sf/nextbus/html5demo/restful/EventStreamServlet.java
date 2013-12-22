/**
 * *****************************************************************************
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
import java.util.Random;

/**
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
 * http://dev.w3.org/html5/eventsource/
 *
 * @author jrd
 *
 */
@WebServlet(name = "EventStreamServlet", urlPatterns = {"/eventstream"})
public class EventStreamServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(EventStreamServlet.class);
    Random r = new Random();

    private String sseIdentifierString = "";
    private Long retry = 1000L;

    @Inject
    SessionProxy sessionProxy;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Reject the request if it is not compatible with HTML 5 event stream
        String accept = request.getHeader("Accept");
        String lastEventId = request.getHeader("Last-Event-ID");
        if (accept.equalsIgnoreCase("text/event-stream") == false) {
            log.warn("Invalid client request. HTML 5 SSE can only serve 'text/event-stream' , not {} Sending HTTP 204.", new Object[]{accept});
            response.getWriter().println("Invalid client request. HTML 5 SSE can only serve 'text/event-stream' , not " + accept + ".");
            response.setStatus(java.net.HttpURLConnection.HTTP_NO_CONTENT);
            response.flushBuffer();
            return;
        }

        // if there is no web-container session associated with the call, terminate connection.
        // You must send an HTTP 204 to the client to prevent it from reconnect/retry cycles.
        // Otherwise, log the connection (or re-connection)
        ClientEventStream evStream = null;
        if (request.getSession(false) == null) {
            response.setStatus(java.net.HttpURLConnection.HTTP_NO_CONTENT);
            log.error("No web session present ; cannot continue on behalf of SSE client. Sending HTTP 204.");
            response.flushBuffer();
            return;
        } else {
            evStream = sessionProxy.getEventStreamQueue();
            log.info("Client connection for SSE Session {} Last Event Id {} Principal {}.", new Object[]{request.getSession().getId(), lastEventId, request.getUserPrincipal()});
            evStream.reconnect();
        }

        // The standard Server-Send Events wire protocol response prologue....
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/event-stream");
        response.setHeader("connection", "keep-alive");
        response.getWriter().format(": %s\n", sseIdentifierString);
        response.getWriter().format("retry: %d\n", retry);
        response.getWriter().println();
        response.flushBuffer();

        // Yes - it's an infinite loop!
        try {
            while (true) {
                try {
                    EventEntry e = evStream.getNextEventToSend();
                    simBugs();
                    response.getWriter().println(evStream.toJSON(e));
                    response.getWriter().println(); // Event Stream REQUIRES newline between msg blocks.
                    response.setStatus(java.net.HttpURLConnection.HTTP_OK);
                    response.flushBuffer();
                    // once the item has been transmitted, and flush buffer succeeds, dequeue....
                    evStream.acknowledgeSent(e);
                    log.debug("sent event");
                } catch (InterruptedException waiterr) {
                    log.warn("thread wait in getNextEvent() was interrupted. Continuing. ");
                }
            } //end-forever-while
        } catch (IOException any) {
            log.error("IO Exception while sending SSE to client. Closing connection. ", any);
            response.getWriter().close();
        } catch (Exception anyOther) {
            log.error("Exception during Event Stream polling. Sending HTTP 500 to SSE client.", anyOther);
            response.setStatus(java.net.HttpURLConnection.HTTP_INTERNAL_ERROR);
            response.flushBuffer();
        }
    }

    /**
     * This is used for testing & development to intentionally inject Exceptions
     * to see if the HTML 5 client can respond properly to HTTP error status
     * codes, and negotiate reconnection.
     *
     * @throws Exception
     */
    public void simBugs() throws Exception {
        if (r.nextDouble() > 0.9) {
            throw new IOException("Simulating Errors.");
        }
        if (r.nextDouble() > 0.75) {
            throw new RuntimeException("Simulating Errors.");
        }
    }

    @Override
    public String getServletInfo() {
        return "HTML 5 Server Side Event Servlet: " + sseIdentifierString;
    }
}
