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
import net.sf.nextbus.publicxmlfeed.service.INextbusService;
import net.sf.nextbus.publicxmlfeed.domain.Stop;
import net.sf.nextbus.publicxmlfeed.domain.PredictionGroup;
import net.sf.nextbus.publicxmlfeed.domain.Prediction;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Task Worker to fetch Predictions on behalf of a Service Activator Task.
 *
 * @author jrd
 */
public class PredictionTaskWorker extends TaskWorker {

    final Logger log = LoggerFactory.getLogger(PredictionTaskWorker.class);
    private INextbusService nextbus;
    private List<Stop> stops;

    public PredictionTaskWorker(INextbusService serviceEndpoint, List<Stop> stops, Long refresh) {
        super.limit = refresh;
        nextbus = serviceEndpoint;
        this.stops = stops;
    }

    @Override
    public Collection execute() {
        try {
            List<Prediction> rv = new java.util.ArrayList<Prediction>();
            long last = System.currentTimeMillis();
            List<PredictionGroup> predictionGroups = nextbus.getPredictions(stops);
            for (PredictionGroup pg : predictionGroups) {
                log.trace(predictionGroups.size() + " prediction groups obtained for multistop call.");
                // For each group - usually corresponding to a stop
                for (PredictionGroup.PredictionDirection direction : pg.getDirections()) {
                    // For each direction (inbound, outbound)
                    for (Prediction p : direction.getPredictions()) {
                        rv.add(p);
                    }
                }
            }
            this.success++;
            this.lastTime = last;
            return rv;
        } catch (net.sf.nextbus.publicxmlfeed.service.ServiceException se) {
            errors++;
            log.warn("Fault while obtaining prediction update. ", se);
            return empty();
        }
    }
}
