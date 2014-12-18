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
package com.elliottsj.nextbus.impl;
import com.elliottsj.nextbus.service.ServiceException;

/**
 * An Abstraction to allow pluggable HTTP RPC providers for the Web Service client.
 * In the Java EE Universe, an implementer might use either Apache Commons HttpClient
 * for communications due to its flexibility. In other situations, the java.net.URLConnection
 * based client might be required to be use such as in WebSphere where application connections
 * and security are managed by the container. In the Mobile Phone world such as Android OS,
 * you'd not be able to use Commons HttpClient and would be required to use the Platform's
 * socket factory for output HTTP connections.
 * 
 * 
 * @author jrd
 */
public interface RPCImpl {
     
    /**
     * It is the responsibility of the implementer to convert whichever
     * underlying communications layer exceptions into a ServiceException.
     * 
     * @param request the Request parameters object
     * @return the XML response stream from the REST-ful web service
     * @throws com.elliottsj.nextbus.service.ServiceException
     */
    public String call(RPCRequest request) throws ServiceException;
    
}
