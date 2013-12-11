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
package net.sf.nextbus.publicxmlfeed.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Base Class for NextBus Value Objects - Temporal, Serializable and must carry Copyright text.
 *
 * @author jrd (modified by elliottsj)
 */
public abstract class NextBusValueObject implements Serializable, TemporalValueObject {

    /** Timestamp to implement TemporalValueObject */
    protected final Long createTimeUtc;

    /**
     * Implicit constructor will set the birth timestamp of this instance to System.currentTime
     */
    public NextBusValueObject() {
        createTimeUtc = System.currentTimeMillis();
    }

    /**
     * VehicleLocation and Prediction all require adjustment of the birth date to some time in the recent past.
     *
     * @param birthday milliseconds since the Unix epoch
     */
    protected NextBusValueObject(long birthday) {
        createTimeUtc = birthday;
    }

    /** The following is an implementation of the Temporal interface */

    /**
     * Gets the creation timestamp of the object in milliSeconds since 1 January 1970 00:00:00 UTC
     *
     * @return birth date from the Unix epoch.
     */
    public long getObjectTimestamp() {
        return createTimeUtc;
    }

    /**
     * Gets the creation timestamp of the object
     *
     * @return the creation timestamp of the object
     */
    public Date getTimestamp() {
        return new Date(createTimeUtc);
    }

    /**
     * Gets the current age of the object in Seconds.
     *
     * @return current age of the object in Seconds.
     */
    public long getObjectAge() {
        return (System.currentTimeMillis() - createTimeUtc) / 1000;
    }

    /**
     * Tests the age of object since its creation time.
     *
     * @param seconds a number of seconds
     * @return true iff the object is currently older than 'seconds' given
     */
    public boolean isObjectOlderThan(long seconds) {
        return getObjectAge() > seconds;
    }

}
