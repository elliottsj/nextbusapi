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
 * Indicates a temporary, recoverable failure of the application either due to a
 * network connectivity issue or, when NextBus sends an &lt;Error
 * shouldRetry='true'&gt; condition. It is up to the consuming application to
 * implement any wait-retry processing.
 *
 * @author jrd
 */
public class TransientServiceException extends ServiceException {

    private static final long serialVersionUID = -8583454976984466398L;

    private boolean shouldRetry = false;

    public TransientServiceException(String reason, Throwable rootCause) {
        super(reason, rootCause);
    }
    public TransientServiceException(Throwable rootCause) {
        super(rootCause);
    }
    public TransientServiceException(String reason) {
        super(reason);
    }
    public TransientServiceException(boolean retry, String reason) {
        super(reason);
        shouldRetry = retry;
    }

    /**
     * @return true iff NextBus Web Service indicated in the Error tag that shouldRetry is doable.
     */
    public boolean isShouldRetry() {
        return shouldRetry;
    }

}
