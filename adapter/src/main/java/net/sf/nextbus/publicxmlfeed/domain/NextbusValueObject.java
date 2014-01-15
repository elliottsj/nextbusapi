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
 * Base Class for NextBus value objects - Temporal, Serializable and must carry copyright text.
 * 
 * @author jrd
 * @author elliottsj
 */
public abstract class NextbusValueObject implements Serializable, TemporalValueObject {

    private static final long serialVersionUID = -4066053159634371148L;

    /** Timestamp to implement TemporalValueObject */
    protected final long timestamp;

    /** Copyright notice for Value object payload - assigned by either the Transit Agency or NextBus, or both. */
    protected String copyright;
    
    /**
     * Set the timestamp and the copyright text
     *
     * @param copyright Copyright text provided by NextBus.
     * @param timestamp Epoch milliseconds when this object was created. If {@code null}, then the current time will be used.
     */
    protected NextbusValueObject(String copyright, Long timestamp) {
        this.timestamp = timestamp != null ? timestamp : System.currentTimeMillis();
        this.copyright = copyright;
    }

    /**
     * The license requires carrying the copyright notice along with the
     * data. Nextbus does not own the copyright on the stream data, rather
     * the regional transit companies that use nextbus. In cases where the
     * data has copyright, the Service implementation must insert whatever
     * value is included in the &lt;body&gt; element into each object fabricated
     * from the response.
     */
    public final String getCopyright() { return copyright; }

    @Override
    public final long getTimestamp() {
        return timestamp;
    }

    @Override
    public final long getAge() {
        return System.currentTimeMillis() - timestamp;
    }

    @Override
    public final boolean isObjectOlderThan(long milliseconds) {
        return getAge() > milliseconds;
    }

}
