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

/**
 * A Transit agency whom has route, schedule, vehicle location and prediction
 * data served by NextBus.
 *
 * @author jrd
 */
public class Agency extends NextbusValueObject implements Comparable<Agency> {

    static final long serialVersionUID = -3914701507430700194L;
    /**
     * Key identifier - example rutgers
     */
    private String id;
    /**
     * Full display title - example Rutgers University
     */
    private String title;
    /**
     * Short display title - example Rutgers
     */
    private String shortTitle = "";
    /**
     * Region value example New Jersey
     */
    private String regionTitle = "";

    /**
     * Serialization ctor.
     */
    protected Agency() {
    }

    /**
     * Immutable domain class constructor.
     *
     * @param _id
     * @param _title
     * @param _shortTitle
     * @param _regionTitle
     * @param copyRighttext
     */
    public Agency(String _id, String _title, String _shortTitle, String _regionTitle, String copyRighttext) {
        super(copyRighttext);
        id = _id;
        title = _title;
        if (_shortTitle != null) {
            shortTitle = _shortTitle;
        }
        if (_regionTitle != null) {
            regionTitle = _regionTitle;
        }
    }

    /**
     * Get the unique ID (example tag value)
     *
     * @return example 'mbta'
     */
    public String getId() {
        return id;
    }

    /**
     * Get the full display title.
     *
     * @return example 'Massachusetts Bay Transit Authority'
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the region served for the transit agency.
     *
     * @return example Massachusetts
     */
    public String getRegionTitle() {
        return regionTitle;
    }

    /**
     * Get the short title, if any ; This attribute is often an empty string.
     *
     * @return The NextBus spec recommends "If no shortTitle element is
     * provided, simply use the standard title element" (page 8)
     */
    public String getShortTitle() {
        return shortTitle;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Agency other = (Agency) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Agency{" + "id=" + id + ", title=" + title + ", shortTitle=" + shortTitle + ", regionTitle=" + regionTitle + '}';
    }

    public int compareTo(Agency o) {
        return this.title.compareTo(o.title);
    }
    
}
