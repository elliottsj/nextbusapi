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
package net.sf.nextbus.jmspump.sender;

import java.util.Collection;
import java.util.List;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.VehicleLocation;
import net.sf.nextbus.publicxmlfeed.service.INextbusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Task Worker to fetch VehicleLocation on behalf of a Spring Integration ServiceActivated Task.
 * @author jrd
 */
public class VehicleLocationTaskWorker extends TaskWorker {

    final Logger log = LoggerFactory.getLogger(VehicleLocationTaskWorker.class);
    private INextbusService nextbus;
    private Route route;
    
    public VehicleLocationTaskWorker(INextbusService webService, Route arg, Long refreshInterval) {
        super.limit=refreshInterval;
        this.nextbus=webService;
        this.route=arg;
    }
    
    @Override
    public Collection execute() {
        if (! enabled) return empty();
        try {
            List<VehicleLocation> vl = nextbus.getVehicleLocations(route, 0);

            lastTime = System.currentTimeMillis();
            success++;
            log.debug("got update for " + route + " with " + vl.size() + " vehicle locations.");
            return vl;
        } catch (net.sf.nextbus.publicxmlfeed.service.ServiceException se) {
            errors++;
            log.warn("Fault while obtaining vehicle location update for " + route, se);
            return empty();
        }
    }
}
