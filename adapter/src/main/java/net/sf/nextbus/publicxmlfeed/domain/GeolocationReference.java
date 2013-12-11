/*******************************************************************************
 * Copyright (C) 2011-2013 by James R. Doyle
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
 *
 * @author jrd
 */
public class GeolocationReference implements Serializable {
    private final Geolocation referencePoint;
    private final Geolocation samplePoint;
    private final Double distance;
    
    public GeolocationReference(Geolocation ref, Geolocation pt) {
        referencePoint = ref;
        samplePoint = pt;
        distance = ref.getDistanceInKm(pt);
    }

    public Geolocation getReferencePoint() {
        return referencePoint;
    }

    public Geolocation getSamplePoint() {
        return samplePoint;
    }

    public Double getDistance() {
        return distance;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.referencePoint != null ? this.referencePoint.hashCode() : 0);
        hash = 29 * hash + (this.samplePoint != null ? this.samplePoint.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GeolocationReference other = (GeolocationReference) obj;
        if (this.referencePoint != other.referencePoint && (this.referencePoint == null || !this.referencePoint.equals(other.referencePoint))) {
            return false;
        }
        if (this.samplePoint != other.samplePoint && (this.samplePoint == null || !this.samplePoint.equals(other.samplePoint))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ranger{" + "referencePoint=" + referencePoint + ", samplePoint=" + samplePoint + ", distance=" + distance + '}';
    }
    
}