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
package com.elliottsj.nextbus.service;

/**
 * Indicates a fatal, functional failure of the service likely caused by 
 * XML incompatibility between this adapter module and the Nextbus service.
 * In most cases, it would be the result of a change of the XML wire protocol
 * being incompatible with the XSD specifications used to build this module.
 * 
 * 
 * @author jrd
 */
public class FatalServiceException extends ServiceException {
   
    public FatalServiceException(String reason) {
        super(reason);
    }
    public FatalServiceException(Throwable rootCause) { 
        super(rootCause);
    }
    public FatalServiceException(String reason, Throwable rootCause) {
        super(reason, rootCause);
    }
    
}
