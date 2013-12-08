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
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.List;

/**
 * Schedule composite for the NextBus Schedule service.
 * 
 * @author jrd
 */
public class DailySchedule extends Schedule {
 
     static final long serialVersionUID = 4090870366793806592L;
     
   /** Domain factory ctor */
   public DailySchedule(Route parent, List<String> stopIds, List<Block> _blocks, String schedClass, String svcClass, String _direction) {   
        this.route = parent;
        this.stopIdsServed = stopIds;
        this.scheduleBlocks = _blocks;
        this.direction = _direction;
        this.serviceClass = svcClass;
        this.scheduleClass = schedClass;
    }
}
