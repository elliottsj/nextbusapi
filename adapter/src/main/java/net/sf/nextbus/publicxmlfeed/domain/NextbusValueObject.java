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

/**
 * Base Class for NextBus Value Objects - Temporal, Serializable and must carry Copyright text.
 * 
 * @author jrd
 */
public abstract class NextbusValueObject implements Serializable, TemporalValueObject {
 
    
    static final long serialVersionUID = -2133869814808339041L;
    /** Timestamp to implement TemporalValueObject */ 
    protected final Long createTimeUtc;
    /** Copyright notice for Value object payload - assigned by either the Transit Agency or NextBus, or both. */
    protected String copyrightNotice;
    
    /**
     * Implicit ctor will set the birth timestamp of this instance to System.currentTime
     */
    public NextbusValueObject() {
        createTimeUtc = System.currentTimeMillis();
        copyrightNotice="";
    }
    /**
     * Implicit ctor with Copyright Notice text
     * @param cpyRtText 
     */
    public NextbusValueObject(String cpyRtText) {
        this();
        copyrightNotice = cpyRtText;
    }
    
    /**
     * VehicleLocation and Prediction all require adjustment of the birthdate to some time in the recent past.
     * @param birthday 
     */
    protected NextbusValueObject(long birthday) {
        createTimeUtc = birthday;
    }
    /**
     * Set the birthday and the copyright text... ugh.
     * @param birthday
     * @param cpyRtText 
     */
    protected NextbusValueObject(long birthday, String cpyRtText) {
        createTimeUtc = birthday;
        copyrightNotice = cpyRtText;
    }
    
    /**
     * The license requires carrying the copyright notice along with the
     * data. Nextbus does not own the copyright on the stream data, rather
     * the regional transit companies that use nextbus. In cases where the
     * data has copyright, the Service implementation must insert whatever
     * value is included in the <body> element into each object fabricated
     * from the response.
     */
    public final String getCopyrightNotice() { return copyrightNotice; }

    /* The following are implementations of the Temporal interface. */
    
    public final long getObjectAge() {
        return (System.currentTimeMillis()-createTimeUtc)/1000;
    }

    public final long getObjectTimestamp() {
        return createTimeUtc;
    }

    public final boolean isObjectOlderThan(long seconds) {
        return getObjectAge()>seconds;
    }
   
    
    
}
