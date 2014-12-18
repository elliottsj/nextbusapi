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
package com.elliottsj.nextbus.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A Transit agency served by NextBus.
 *
 * @author jrd
 * @author elliottsj
 */
@SuppressWarnings("UnusedDeclaration")
@DatabaseTable(tableName = "agencies")
public class Agency extends NextbusValueObject implements Comparable<Agency> {

    private static final long serialVersionUID = -7910786609872379992L;

    public static final String FIELD_TAG = "tag";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_SHORT_TITLE = "short_title";
    public static final String FIELD_REGION_TITLE = "region_title";

    /**
     * Key identifier - example rutgers
     */
    @DatabaseField(columnName = FIELD_TAG, canBeNull = false)
    private String tag;

    /**
     * Full display title - example Rutgers University
     */
    @DatabaseField(columnName = FIELD_TITLE, canBeNull = false)
    private String title;

    /**
     * Short display title - example Rutgers
     */
    @DatabaseField(columnName = FIELD_SHORT_TITLE)
    private String shortTitle;

    /**
     * Region value example New Jersey
     */
    @DatabaseField(columnName = FIELD_REGION_TITLE, canBeNull = false)
    private String regionTitle;

    /**
     * Empty constructor for OrmLite
     */
    Agency() {
    }

    /**
     * Full constructor
     *
     * @param tag the ID of this agency
     * @param title the title of this agency
     * @param shortTitle the short title of this agency
     * @param regionTitle the region title of this agency
     * @param copyright the copyright text provided by NextBus
     * @param timestamp epoch milliseconds when this agency was created
     */
    public Agency(String tag, String title, String shortTitle, String regionTitle, String copyright, Long timestamp) {
        super(copyright, timestamp);
        this.tag = tag;
        this.title = title;
        this.shortTitle = shortTitle;
        this.regionTitle = regionTitle;
    }

    /**
     * Domain factory constructor
     */
    public Agency(String tag, String title, String shortTitle, String regionTitle, String copyright) {
        this(tag, title, shortTitle, regionTitle, copyright, null);
    }

    /**
     * Gets the unique tag of this route.
     * Example: 'ttc'
     *
     * @return the unique tag of this agency
     */
    public String getTag() {
        return tag;
    }

    /**
     * Gets the full display title.
     * Example: 'Toronto Transit Commission'
     *
     * @return the full display title of this agency
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the short title if it exists, otherwise {@code null}.
     * Example: 'Toronto TTC'
     * <br>
     *
     * The NextBus spec recommends "If no shortTitle element is
     * provided, simply use the standard title element" (page 8)
     *
     * @return the short title of this agency if it exists, otherwise {@code null}
     */
    public String getShortTitle() {
        return shortTitle;
    }

    /**
     * Gets the region served by this transit agency.
     * Example: 'Massachusetts'
     *
     * @return the region served by this agency
     */
    public String getRegionTitle() {
        return regionTitle;
    }

    /**
     * Agency identity is composite of only the tag.
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
        return String.format("Agency{tag=%s, title=%s, shortTitle=%s, regionTitle=%s}",
                             tag, title, shortTitle, regionTitle);
    }

    /**
     * Compare this agency's title with another agency's title for
     * the purpose of alphabetical sorting
     *
     * @param o another agency
     * @return a negative integer if this agency precedes {@code o},
     *         zero if their titles are equivalent,
     *         or a positive integer if this agency succeeds {@code o}
     */
    @Override
    public int compareTo(Agency o) {
        return this.title.compareTo(o.title);
    }

}
