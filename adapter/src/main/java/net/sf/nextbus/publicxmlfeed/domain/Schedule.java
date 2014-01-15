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
import java.util.List;

/**
 * Abstract base class and supporting nested classes for the NextBus Schedule service. 
 * @author jrd
 */
public abstract class Schedule extends NextbusValueObject {
    
    /**
     * Domain factory ctor.
     */
    public Schedule(Route parent, List<String> stopIds, List<Block> _blocks, String schedClass, String svcClass, String _direction, String copyright) {
        super(copyright, null);
        this.route = parent;
        this.stopIdsServed = stopIds;
        this.scheduleBlocks = _blocks;
        this.direction = _direction;
        this.serviceClass = svcClass;
        this.scheduleClass = schedClass;
               
    }
    
    /** The Route that owns this schedule instance. */
    protected Route route;
    /** Schedule classifier - freeform value from Nextbus - i.e. '20120324' */
    protected String scheduleClass;
    /** Service classifier - freeform value from Nextbus - i.e. MoTuWeThFr, Saturday, SaturdayHoliday, WkdayNoSchool */
    protected String serviceClass;  
    /** Direction value - freeform from Nextbus - i.e. Inbound, Outbound, Express, etc*/
    protected String direction; 
    /** Stops served listed by tag (id) Values rather than whole Stop objects.*/
    protected List<String> stopIdsServed;
    /** Schedule Blocks bound to this Schedule element. */
    protected List<Block> scheduleBlocks;
    
    
    /**
     * A Schedule Block contains a set of Scheduled Stops
     */
    public static class Block implements java.io.Serializable, Comparable<Block> {
        static final long serialVersionUID = -6841575477878963644L;
        protected Block() { }
        public Block(String id, List<StopSchedule> _stops) {
            this.blockId=id;   this.scheduledStops = _stops;
        }
        /** The Schedule Block ID from NextBus, appears to be a distinct value but not intended for human consumption ; i.e. 'B34E_234' */
        protected String blockId;
        /** The scheduled and skipped Stops and Times on this Schedule Block */
        protected List<StopSchedule> scheduledStops;
        /**
         * @return the Block ID
         */
        public String getBlockId() {
            return blockId;
        }
        /**
         * @return The Scheduled Stops for this Block 
         */
        public List<StopSchedule> getScheduledStops() {
            return scheduledStops;
        }

        public int compareTo(Block o) {
            return this.blockId.compareTo(o.blockId);
        }
        
        
        
    
        
        /**
         * A Stop on a Schedule
         */
        public abstract static class StopSchedule implements java.io.Serializable {
            static final long serialVersionUID = 2644426041838771749L;
            /** The Stop id */
            protected String stopId;
            /** Indicates that this Stop, ordinarily served by a bus, is Skipped by busses running on this schedule - i.e. for Express service routes */
            protected boolean skipped;
            
            protected StopSchedule() { }
            protected StopSchedule(String id, boolean arg) {
                stopId=id;
                skipped=arg;
            }
            /**
             * @return true if this Stop is SKIPPED in the travel run of this particular schedule.
             */
            public boolean isSkipped() {
                return skipped;
            }

            public String getStopId() {
                return stopId;
            }

            
            
        }
        /**
         * A Skipped stop (i.e. express routes, weekend routes etc)
         */
        public static class SkippedStop extends StopSchedule implements Comparable<SkippedStop> {
            static final long serialVersionUID = 4893250953683420659L;
            public SkippedStop(String stopId) {
                super(stopId, true);
            }
            public int compareTo(SkippedStop o) {
                return this.stopId.compareTo(o.stopId);
            }
        }
        
        /**
         * A Scheduled stop.
         */
        public static class StopScheduleTime extends StopSchedule implements Comparable<StopScheduleTime> {
            static final long serialVersionUID = 7293226153133025193L;
            /** Schedule wall-clock time in Hours - in the local time zone where the service occurs. */
            protected Short stopTimeHour;
             /** Schedule wall-clock time in Minutes - in the local time zone where the service occurs. */
            protected Short stopTimeMinutes;
            
            protected StopScheduleTime() {} 
            /**
             * Ctor for DomainFactory.
             * TODO:  This element is constructed from data like this: <stop tag="61871" epochTime="65700000">18:15:00</stop>
             * 
             * @param id
             * @param hour local timezone hour
             * @param minutes local timezone minutes
             */
            public StopScheduleTime(String id, Short hour, Short minutes) {
                super(id,false);
                stopId=id;  
                this.stopTimeHour=hour; this.stopTimeMinutes=minutes;
            }
            /**
             * @return The Id of the Stop, not the Printed title ; Use RouteConfig to resolve to a full Stop class. 
             */
            public String getStopId() {
                return stopId;
            }
            /**
             * 
             * @return Schedule Hour - in 24 hour format (0..23) in the local Timezone in force at this location.
             */
            public Short getStopTimeHour() {
                return stopTimeHour;
            }
            /**
             * 
             * @return Schedule Minutes in the Hour (0..59)
             */
            public Short getStopTimeMinutes() {
                return stopTimeMinutes;
            }
            public int compareTo(StopScheduleTime o) {
                return this.stopTimeHour.compareTo(o.stopTimeHour) *60 + this.stopTimeMinutes.compareTo(o.stopTimeMinutes);
            }   
        }
    }

    public List<Block> getScheduleBlocks() {
        return scheduleBlocks;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Schedule other = (Schedule) obj;
        if (this.route != other.route && (this.route == null || !this.route.equals(other.route))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.route != null ? this.route.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Schedule{" + "route=" + route + ", scheduleClass=" + scheduleClass + ", serviceClass=" + serviceClass + ", direction=" + direction + ", stopIdsServed=" + stopIdsServed + ", scheduledStops=" + scheduleBlocks.size() + '}';
    }
    
    
}
