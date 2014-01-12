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

    private static final long serialVersionUID = -4332318517973445125L;

    /**
     * Key identifier - example rutgers
     */
    private String tag;

    /**
     * Full display title - example Rutgers University
     */
    private String title;

    /**
     * Short display title - example Rutgers
     */
    private String shortTitle;

    /**
     * Region value example New Jersey
     */
    private String regionTitle;

    /**
     * Full constructor
     *
     * @param tag the ID of this agency
     * @param title the title of this agency
     * @param shortTitle the short title of this agency
     * @param regionTitle the region title of this agency
     * @param copyrightText the copyright text provided by NextBus
     * @param timestamp epoch milliseconds when this agency was created
     */
    public Agency(String tag, String title, String shortTitle, String regionTitle, String copyrightText, long timestamp) {
        super(timestamp, copyrightText);
        this.tag = tag;
        this.title = title;
        this.shortTitle = shortTitle != null ? shortTitle : "";
        this.regionTitle = regionTitle;
    }

    /**
     * Constructor without short title
     */
    public Agency(String tag, String title, String regionTitle, String copyrightText) {
        this(tag, title, null, regionTitle, copyrightText);
    }

    /**
     * Immutable domain class constructor.
     */
    public Agency(String tag, String title, String shortTitle, String regionTitle, String copyrightText) {
        super(copyrightText);
        this.tag = tag;
        this.title = title;
        this.shortTitle = shortTitle != null ? shortTitle : "";
        this.regionTitle = regionTitle;
    }

    /**
     * Get the unique ID (example tag value)
     *
     * @return example 'mbta'
     */
    public String getTag() {
        return tag;
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

    /**
     * Agency identity is composite of only the ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agency)) return false;

        Agency agency = (Agency) o;

        return tag.equals(agency.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }

    @Override
    public String toString() {
        return "Agency{" + "tag=" + tag + ", title=" + title + ", shortTitle=" + shortTitle + ", regionTitle=" + regionTitle + '}';
    }

    public int compareTo(Agency o) {
        return this.title.compareTo(o.title);
    }

}
