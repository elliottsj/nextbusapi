/**
 * *****************************************************************************
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
 * Usage of the NextBus Web Service and its data is subject to separate Terms
 * and Conditions of Use (License) available at:
 *
 * http://www.nextbus.com/xmlFeedDocs/NextBusXMLFeed.pdf
 *
 *
 * NextBus® is a registered trademark of Webtech Wireless Inc.
 *
 *****************************************************************************
 */
package net.sf.nextbus.publicxmlfeed.domain;

import java.util.GregorianCalendar;

/**
 * A NextBus Arrival/Departure Time Prediction element for a Stop.
 *
 * <pre>
 * <prediction epochTime="1337181721123" seconds="257" minutes="4" isDeparture="false" affectedByLayover="true" dirTag="39_1_var1" vehicle="1041" block="S39_46" tripTag="17300212" />
 * </pre>
 *
 * @author jrd
 */
public class Prediction extends NextbusValueObject implements Comparable<Prediction> {

    static final long serialVersionUID = 8892241960475516950L;
    
    /**
     * The Stop to that owns this Prediction element.
     */
    private Stop parent;
    /**
     * The Vehicle for which the time is predicted - see Vehicle Location
     * service as well.
     */
    private Vehicle vehicle;
    /**
     * The ID of the Direction element this Vehicle is traveling
     */
    private String directionId;
    /**
     * The Arrival of Departure time in UTC (Zulu) time - NOT the Local
     * Timezone!
     */
    private Long predictedArrivalOrDepartureTimeUTC;
    /**
     * Is the predicted time for a bus Departure (true) or an Arrival (false)?
     */
    private boolean predictionForDepartureTime;
    /**
     * Does the prediction estimate include time alloted for Station layover ?
     */
    private boolean predictionIncludesLayoverEstimate;
    /**
     * true if
     */
    private boolean delayed;
    /**
     * true if the Prediction is based on the Schedule; false if prediction
     * based on GPS-driven telemetry and estimation.
     */
    private boolean scheduleBasedPrediction;
    /**
     * The Schedule Block ID this Vehicle is currently traveling on - see
     * Schedule Service
     */
    private String block;
    /**
     * The trip tag - transit agency assigned.
     */
    private String tripTag;
    /**
     * The brach tag - transit agency assigned.
     */
    private String branch;

    /**
     * Serialization ctor
     */
    protected Prediction() {
    }

    /**
     * Domain factory ctor.
     */
    public Prediction(Stop stop, String _vehId, String _dirnId, boolean _departure, boolean _layover, String _tripTag, String _block, long predictedTime, String copyright, boolean _delayed, boolean _scheduleBased) {
        super(copyright);
        this.parent = stop;
        this.vehicle = new Vehicle(_vehId);
        this.directionId = _dirnId;
        this.block = _block;
        this.tripTag = _tripTag;
        this.predictionForDepartureTime = _departure;
        this.predictionIncludesLayoverEstimate = _layover;
        this.predictedArrivalOrDepartureTimeUTC = predictedTime;
        this.delayed = _delayed;
        this.scheduleBasedPrediction = _scheduleBased;
    }

    /**
     *
     * @return true if this is a Departure time prediction.
     */
    public boolean isDepartureTimePrediction() {
        return predictionForDepartureTime;
    }

    /**
     * It might seem silly to have to getters() for the same attribute, but for
     * people who will code against this Domain class, it makes for nicer
     * readability.
     *
     * @return true if this is an Arrival time prediction
     */
    public boolean isArrivalTimePrediction() {
        return !(predictionForDepartureTime);
    }

    /**
     *
     * @return true if the predicted time includes a layover stop estimation.
     */
    public boolean isPredictionIncludesLayoverEstimate() {
        return predictionIncludesLayoverEstimate;
    }

    /**
     * the bus is not traveling as fast as expected over the last few minutes.
     * This is useful for determining if a vehicle is stuck in traffic such that
     * the predictions might not be as accurate. This feature is only enabled
     * for certain agencies. This element is only included in the XML feed when
     * the value is true.
     *
     * @return
     */
    public boolean isDelayed() {
        return delayed;
    }

    /**
     * Specifies whether the predictions are based solely on the schedule and do
     * not take the GPS position of the vehicle into account. This feature is
     * not currently available for TTC. This element is only included in the XML
     * feed when the value is true. If the value is not set then it should be
     * considered false.
     *
     * @return
     */
    public boolean isScheduleBasedPrediction() {
        return this.scheduleBasedPrediction;
    }
    
    /**
     * Indicates the prediction is determined by a NextBus proprietary heuristic based on GPS telemetry.
     * @return 
     */
    public boolean isHeuristicBasedPrediction() {
        return ! scheduleBasedPrediction;
    }

    /**
     *
     * @return The predicted Arrival or Depature time in milliseconds since 1
     * Jan 1970 00:00 UTC.
     */
    public long getPredictedArrivalOrDepartureTimeUTC() {
        return predictedArrivalOrDepartureTimeUTC;
    }

    /**
     * Derived value:
     *
     * @return Seconds until the Arrival (or departure).
     */
    public long getPredictedArrivalOrDepartureSeconds() {
        return (predictedArrivalOrDepartureTimeUTC - System.currentTimeMillis()) / 1000;
    }

    /**
     * Derived value
     *
     * @return Minutes until the Arrival (or departure).
     */
    public long getPredictedArrivalOrDepartureMinutes() {
        return getPredictedArrivalOrDepartureSeconds() / 60;
    }

    /**
     *
     * @return The vehicle tag on this Route and Direction.
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     *
     * @return The direction ID.
     */
    public String getDirectionId() {
        return directionId;
    }

    /**
     *
     * @return The Stop to which this prediction estimate belongs.
     */
    public Stop getParent() {
        return parent;
    }

    /**
     *
     * @return the Trip tag value
     */
    public String getTripTag() {
        return tripTag;
    }

    /**
     *
     * @return The Block tag value
     */
    public String getBlock() {
        return block;
    }

    /**
     *
     * @return The Branch value, if set
     */
    public String getBranch() {
        return branch;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Prediction other = (Prediction) obj;
        if (this.parent != other.parent && (this.parent == null || !this.parent.equals(other.parent))) {
            return false;
        }
        if (this.vehicle != other.vehicle && (this.vehicle == null || !this.vehicle.equals(other.vehicle))) {
            return false;
        }
        if ((this.directionId == null) ? (other.directionId != null) : !this.directionId.equals(other.directionId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.parent != null ? this.parent.hashCode() : 0);
        hash = 41 * hash + (this.directionId != null ? this.directionId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Prediction{" + "parent=" + parent + ", vehicle=" + vehicle + ", directionId=" + directionId + ", predictedArrivalOrDepartureTimeUTC=" + predictedArrivalOrDepartureTimeUTC + ", predictionForDepartureTime=" + predictionForDepartureTime + ", predictionIncludesLayoverEstimate=" + predictionIncludesLayoverEstimate + ", block=" + block + ", tripTag=" + tripTag + ", branch=" + branch + ",delayed=" + delayed + " ,scheduleBased=" + scheduleBasedPrediction + "'}";
    }

    /**
     * Default Comparable uses the prediction time as the metric
     *
     * @param that The Prediction element to compare against
     * @return ordering evaluation for java.lang.Comparable
     */
    public int compareTo(Prediction that) {
        return this.predictedArrivalOrDepartureTimeUTC.compareTo(that.predictedArrivalOrDepartureTimeUTC);
    }
}
