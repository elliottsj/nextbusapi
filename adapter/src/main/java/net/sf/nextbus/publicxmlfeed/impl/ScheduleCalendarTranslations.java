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
package net.sf.nextbus.publicxmlfeed.impl;

import java.util.*;
import net.sf.nextbus.publicxmlfeed.service.ValueConversionException;
/**
 * A Internal Utility Class for translating NextBus Schedule data.
 *
 * @author jrd
 */
class ScheduleCalendarTranslations {

    /**
     * Days of the Week for Nextbus schedules XML serviceClass field
     */
    static enum CalendarDays {

        Sunday("Su", Calendar.SUNDAY),
        Monday("Mo", Calendar.MONDAY),
        Tuesday("Tu", Calendar.TUESDAY),
        Wednesday("We", Calendar.WEDNESDAY),
        Thursday("Th", Calendar.THURSDAY),
        Friday("Fr", Calendar.FRIDAY),
        Saturday("Sa", Calendar.SATURDAY);
        private String abrv;
        private int javaUtilCalendarVal;

        CalendarDays(String abbr, int calVal) {
            abrv = abbr;
            javaUtilCalendarVal = calVal;
        }

        public String getAbrv() {
            return abrv;
        }

        public int getJavaUtilCalendarVal() {
            return javaUtilCalendarVal;
        }

        /**
         * Translates "Mo", "We", "MoTu" etc to an Enum
         *
         * @param arg
         * @return Internal enum value correpsonding to the NextBus day value.
         */
        public static CalendarDays getFoo(String arg) {
            for (CalendarDays days : CalendarDays.values()) {
                if (days.getAbrv().equals(arg)) {
                    return days;
                }
            }
            throw new ValueConversionException("Cannot convert to a Calendar Day. There is no defined Day of Week mapped for abbreviation " + arg);
        }
    }

    /**
     * A Converter to deal with HH:MM:SS schedule times that appear in the schedules XML.
     */
    static class HMSConverter {

        short hh = 0, mm = 0, ss = 0;

        public HMSConverter(String arg) {
            String[] parts = arg.split(":");
            hh = Short.parseShort(parts[0]);
            mm = Short.parseShort(parts[1]);
            if (parts.length == 3) {
                ss = Short.parseShort(parts[2]);
            }
            if (hh < 0) {
                throw new ValueConversionException("Illegal value for time value. hours 0..23 " + arg);
            }
            if (hh > 23) {
                throw new ValueConversionException("Illegal value for time value. hours 0..23 " + arg);
            }
            if (mm < 0) {
                throw new ValueConversionException("Illegal value for time value. minutes 0..59 " + arg);
            }
            if (mm > 59) {
                throw new ValueConversionException("Illegal value for time value. minutes 0..59 " + arg);

            }
            if (ss < 0) {
                throw new ValueConversionException("Illegal value for time value. seconds 0..59 " + arg);
            }
            if (ss > 59) {
                throw new ValueConversionException("Illegal value for time value. seconds 0..59 " + arg);
            }
        }

        public short getHh() {
            return hh;
        }

        public short getMm() {
            return mm;
        }

        public short getSs() {
            return ss;
        }
        
    }

    /**
     *
     * @return Calendar codes for days of the business week
     */
    static List<Integer> getBusinessDays() {
        return unmod(new Integer[]{Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY});
    }

    /**
     *
     * @return Calendar codes for the weekend.
     */
    static List<Integer> getWeekendDays() {
        return unmod(new Integer[]{Calendar.SATURDAY, Calendar.SUNDAY});
    }

    static List<Integer> unmod(Integer[] args) {
        return Collections.unmodifiableList(Arrays.asList(args));
    }
}
