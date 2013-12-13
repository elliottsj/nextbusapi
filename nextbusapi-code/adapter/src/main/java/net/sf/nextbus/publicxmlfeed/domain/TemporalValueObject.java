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
import java.util.Date;

/**
 * In real-time systems, value objects have an additional property of a birthday. Once objects
 * are created, their usefulness in a system begins to decay as the state they reflect may no
 * longer accurately reflect the real world. 
 * 
 * Value objects are frequently cached in memory in a mobile device for performance reasons. Cacheing
 * makes a tradeoff between the communications overhead of always having the most up-to-date state,
 * versus locality which allows for more speedy user-interface.
 * 
 * Likewise, value objects may be tied up in a Message Queueing system and, by the time a recipient
 * receives the message, it may no longer make sense to take further action on outdated state.
 * 
 * @author jrd
 */
public interface TemporalValueObject {
     /**
      * Gets the creation timestamp of the object in milliSeconds since 1 January 1970 00:00:00 UTC
      * @return birthdate from the Unix epoch.
      */
     public long getObjectTimestamp();
     /**
      * Gets the creation timestamp of the object since 1 January 1970 00:00:00 UTC
      * @return 
      */
     public Date getTimestamp();
     
     /**
      * Gets the current age of the object in Seconds.
      * @return current age of the object in Seconds.
      */
     public long getObjectAge();
     
     /**
      * Tests the age of object since its creation time.
      * @param seconds
      * @return true if the object is currently older than 'seconds' given
      */
     public boolean isObjectOlderThan(long seconds);
    
}
