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

import org.simpleframework.xml.Attribute;

/**
 * A Transit agency whom has route, schedule, vehicle location and prediction
 * data served by NextBus.
 */
public class Agency extends NextBusValueObject implements Comparable<Agency> {

    private static final long serialVersionUID = -4008437331349648731L;

    /** Key identifier */
    @Attribute
    private String tag;

    /** Full display title */
    @Attribute
    private String title;

    /** Short display title (optional) */
    @Attribute(required = false)
    private String shortTitle;

    /** Region value */
    @Attribute
    private String regionTitle;

    public Agency(String tag, String title, String shortTitle, String regionTitle) {
        this.tag = tag;
        this.title = title;
        this.shortTitle = shortTitle;
        this.regionTitle = regionTitle;
    }

    /**
     * Get the unique tag.
     *
     * @return example "ttc"
     */
    public String getTag() {
        return tag;
    }

    /**
     * Get the full display title.
     *
     * @return example "Toronto Transit Commission"
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the region served for the transit agency.
     *
     * @return example "Ontario"
     */
    public String getRegionTitle() {
        return regionTitle;
    }

    /**
     * Get the short title, if any; This attribute is often an empty string.
     *
     * @return The NextBus spec recommends "If no shortTitle element is
     * provided, simply use the standard title element" (page 8)
     */
    public String getShortTitle() {
        return shortTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agency)) return false;

        Agency agency = (Agency) o;

        if (!tag.equals(agency.tag)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }

    @Override
    public String toString() {
        return "Agency{" + "tag=" + tag + ", title=" + title + ", shortTitle=" + shortTitle + ", regionTitle=" + regionTitle + '}';
    }

    /**
     * Compares this agency's title with the specified agency's title for alphabetical sorting by title.
     *
     * @param agency the agency to be compared
     * @return a negative integer, zero, or a positive integer as this agency's title is alphabetically before,
     * equal to, or after the specified agency's title.
     */
    public int compareTo(Agency agency) {
        return title.compareTo(agency.title);
    }
    
}
