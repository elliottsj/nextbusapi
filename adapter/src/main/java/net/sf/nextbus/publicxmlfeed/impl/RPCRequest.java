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

import net.sf.nextbus.publicxmlfeed.domain.Stop;
import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.service.FatalServiceException;
import java.util.*;
import java.util.Iterator;
import java.net.URLEncoder;

/**
 * Encapsulates the NextBus Web API invocation conventions.  This class prepares a RESTful Web call
 * by packing call parameters (in the form of Domain objects) into name-value pairs are defined
 * by the NextBus API Document.
 * 
 * @author jrd
 */
public class RPCRequest {

    
    public static final String defaultEndpointUrl = "http://webservices.nextbus.com/service/publicXMLFeed";
    private String endpointUrl;
    Map<String, String> parameters;
    
    /* Turns out predictionsMultiStop HTTP parameters is what breaks an otherwise clean use of Map
     * We need this list to store additional request name-value pairs just for the predictionsMultiStop method
     */
    protected List<String> multiPredictionCornerCase;

    /**
     * Overrides the endpoint URL which has a default value of http://webservices.nextbus.com/service/publicXMLFeed
     * @param arg 
     */
    public void setEndpointUrl(String arg) {
        endpointUrl = arg;
    }

    public RPCRequest() {
        endpointUrl = defaultEndpointUrl;
        parameters = new java.util.HashMap<String, String>();
        multiPredictionCornerCase = new ArrayList<String>();
        
    }

    /**
     * @return request params to invoke 'agencyList' web service method 
     */
    public static RPCRequest newAgencyListCommand() {
        RPCRequest rq = new RPCRequest();
        rq.parameters.put("command", "agencyList");
        return rq;
    }
    
    /**
     * @param a Transit agency to get route catalog from.
     * @return request params to invoke 'routeList' web service method
     */
    public static RPCRequest newRouteListCommand(Agency a) {
        RPCRequest rq = new RPCRequest();
        rq.parameters.put("a", a.getId());
        rq.parameters.put("command", "routeList");
        return rq;
    }

    /**
     * 
     * @param route Route to retrieve configuration for.
     * @return request params to invoke 'routeConfig' web service method 
     */
    public static RPCRequest newRouteConfigCommand(Route route) {
        RPCRequest rq = new RPCRequest();
        rq.parameters.put("a", route.getAgency().getId());
        rq.parameters.put("command", "routeConfig");
        rq.parameters.put("r", route.getTag());
        return rq;
    }

    /**
     * 
     * @param route Route to probe to current vehicle locations.
     * @param deltaT Time range to search for, in milliseconds, up to 15 minutes (900,000 mS)
     * @return request params to invoke 'vehicleLocations' web service method.
     */
    public static RPCRequest newVehicleLocationsCommand(Route route, long deltaT) {
        RPCRequest rq = new RPCRequest();
        rq.parameters.put("a", route.getAgency().getId());
        rq.parameters.put("command", "vehicleLocations");
        rq.parameters.put("r", route.getTag());
        rq.parameters.put("t", Long.toString(deltaT));
        return rq;
    }
    
    

    /**
     * 
     * @param route The Route to retrieve the schudule tree for.
     * @return request params to invoke 'schedule' web services method
     */
    public static RPCRequest newScheduleReqCommand(Route route) {
        RPCRequest rq = new RPCRequest();
        rq.parameters.put("a", route.getAgency().getId());
        rq.parameters.put("command", "schedule");
        rq.parameters.put("r", route.getTag());
        return rq;
        
    }
    
    /**
     * 
     * @param stop Stop to seek arrival/departure predictions for ; single stop
     * @param useShortTitles true to recv Short Title rather than full title. Likely an un-needed feature with less directly coupled UI design.
     * @return request params to invoke 'predictions' web service method 
     */
    public static RPCRequest newPredictionsCommand(Stop stop, boolean useShortTitles) {
         RPCRequest rq = new RPCRequest();
         rq.parameters.put("a", stop.getRoute().getAgency().getId());
         rq.parameters.put("command","predictions");
         rq.parameters.put("r",stop.getRoute().getTag());
         rq.parameters.put("s",stop.getTag());
         if (useShortTitles) rq.parameters.put("useShortTitles","true");
         return rq;
    }
    
    /**
     * 
     * @param agency Transit agency scope
     * @param stopId Alternate Printed Schedule Stop ID
     * @param useShortTitles
     * @return request params to invoke 'predictions' web service method
     */
    public static RPCRequest newPredictionsCommand(Agency agency, String stopId, boolean useShortTitles) {
         RPCRequest rq = new RPCRequest();
         rq.parameters.put("a", agency.getId());
         rq.parameters.put("command","predictions");
         rq.parameters.put("stopId", stopId);
         if (useShortTitles) rq.parameters.put("useShortTitles","true");
         return rq;
    }
    
    /**
     * 
     * @param stops
     * @param useShortTitles
     * @return params to invoke 'predictionsForMultiStops' web service method
     */
    public static RPCRequest newPredictionsCommand(Collection<Stop> stops, boolean useShortTitles) {
         Agency a = stops.iterator().next().getRoute().getAgency();
         
         RPCRequest rq = new RPCRequest();
         rq.parameters.put("a", a.getId());
         rq.parameters.put("command","predictionsForMultiStops");
         if (useShortTitles) rq.parameters.put("useShortTitles","true");
         
         for (Stop s: stops) {
             String param = "s=" + s.getRoute().getTag() + '|' + s.getTag();
             rq.multiPredictionCornerCase.add(param);
         }
         return rq;
    }
    
    /**
     * Returns only the encoded request parameters nugget.
     * @return example "a=1&cz=sdfgdr&tzp=213"
     */
    public String getRequestParametersString() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> params = parameters.keySet().iterator();
        String enc = "US-ASCII";
        do {
            String key = params.next();
            String value = parameters.get(key);
            try {
                sb.append(URLEncoder.encode(key, enc));
                sb.append('=');
                sb.append(URLEncoder.encode(value, enc));
            } catch (java.io.UnsupportedEncodingException encx) {
                // this wont ever happen, but the API forces us to handle this...
                throw new FatalServiceException(encx);
            }
            if (params.hasNext()) {
                sb.append("&");
            }
        } while (params.hasNext());
        // and now... for the dumb corner case - append the last set of args...
        // if we are handling a multi-stop prediction call....
        if (parameters.get("command").equals("predictionsForMultiStops")) {
            for (String param : multiPredictionCornerCase) sb.append(param);
            sb.append("&");
        }
        return sb.toString();
    }

    /**
     * Generates the complete HTTP Request for the REST service endpoint
     * @return Fully encoded HTTP GET URL request string i.e. "http://server/foo/bar?a=1&b=45 ... "
     */
    public String getFullHttpRequest() {
        return endpointUrl + '?' + getRequestParametersString();
    }
}
