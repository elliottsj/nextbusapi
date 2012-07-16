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
package net.sf.nextbus.jmspump.sender;

import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.VehicleLocation;
import net.sf.nextbus.publicxmlfeed.service.INextbusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * Worker Task is responsible for preparing and gathering work to stream to JMS.
 * It needs a simple table of work done in the past, with timestamps, to
 * determine when it's time to replenish JMS with fresh Nextbus state. This insures
 * that we dont excessively probe NextBus.
 * 
 * @author jrd
 */
public class Task {

    final Logger log = LoggerFactory.getLogger(Task.class);
    
    /** The refresh interval in milliseconds, defaults to 60 seconds */
    private Long cacheExpiration = 60*1000L;
    /** Nextbus service adapter */
    private INextbusService nb;
    /** The Nextbus agencies to work on. */
    private Set<Agency> agencies = new HashSet<Agency>();
    /** The Work schedule of Bus Routes */
    private Map<Route, Work> routesToWork = new HashMap();
    private int successfulRuns;
    private int failedRuns;
    
    public void setCacheExpiration(long arg) { 
        cacheExpiration=arg;
    }
    
    /** Keeps track of when Work was last done or attempted for a given Nextbus subject. */
    private class Work {
        long lastTime;
        int errors;
        int success;
        boolean isOld() {
            return System.currentTimeMillis() - lastTime >= cacheExpiration;
        }
    }

    /**
     * Ctor to configure worker to poll all routes for a given agency
     *
     * @param agency
     */
    public Task(INextbusService svc, String agency) {
        nb=svc;
        Agency a = nb.getAgency(agency);
        agencies.add(a);
        configureRoutes(a, null);
    }

    /**
     * Ctor to configure worker to poll a selected set of routes for a given
     * agency
     *
     * @param agency the agency to work, i.e. MBTA
     * @param routes the specific routes to work as a CSV string of Route tags, i.e. "110, 111"
     */
    public Task(INextbusService svc, String agency, String routes) {
        nb=svc;
        Agency a = nb.getAgency(agency);
        agencies.add(a);
        if (routes == null || routes.isEmpty()) {
          configureRoutes(a, null);  
        }
        String [] rs = routes.split(",");
        configureRoutes(a, rs);
    }

    /**
     * Ctor to configure worker to poll all routes for a group of agencies -
     * Warning this will create a substantial burder on the NextBus Service.
     *
     * @param agcs Agencies to poll, as a CSV String i.e. "brooklyn, staten-island,"
     */
    public Task(String agcs) {
        String [] _agencies = agcs.split(",");
        for (String a : _agencies) {
            Agency agency = nb.getAgency(a);
            agencies.add(agency);
        }
    }
    
    

    /**
     * Task Executor, should be called periodically as a Scheduled Task by Spring Integration. 
     * The executor sweeps the Work table, finds things that are stale, and then tries to refresh them
     * by delegating to an appropriate handler method that relies on the NextBus adapter. If errors occur.
     * they are logged, silently counted - but do not interfere with the harvesting job. Thus, this message 
     * pumper is safe against transient nextbus failures.
     */
    public List execute() {
        List worklist = new ArrayList();
        int done=0;
        // todo - create a heartbeat message
        for (Route r : routesToWork.keySet()) {
            Work work = routesToWork.get(r);
            if (work.isOld()) {
                doVehicleLocations(worklist, work, r);
                done++;
            }
        }
        if (done > 0) log.info("nextbus task::execute() replenished state for "+done+" routes with "+worklist.size()+" vehicles found.");
        return worklist;
    }

    /**
     * Refreshes a work item for Vehicle Locations
     * @param work
     * @param r 
     */
    private void doVehicleLocations(List worklist, Work work, Route r) {
        try {
            List<VehicleLocation> vl = nb.getVehicleLocations(r, 0);
            
            work.lastTime = System.currentTimeMillis();
            work.success++;
            // Cant have a list of lists.. Unpack the VehicleLocations and add to the work done list
            for (VehicleLocation v: vl) worklist.add(v);
            successfulRuns++;
            log.debug("got update for "+r.toString()+" with "+vl.size()+" vehicle locations.");
        } catch (net.sf.nextbus.publicxmlfeed.service.ServiceException se) {
            work.errors++;
            log.warn("Fault while obtaining update for "+r, se);
            failedRuns++;
        }
    }
    
    /**
     * Configure the worker schedule for a set of routes, or all routes, for the given agency.
     * @param agency
     * @param routes 
     */
    private void configureRoutes(Agency agency, String[] routes) {
        if (routes != null ) Arrays.sort(routes);
        List<Route> rs = nb.getRoutes(agency);
        for (Route route : rs) {
            if (routes == null || routes.length == 0) {
                routesToWork.put(route, new Work());
            } else {
                if (Arrays.binarySearch(routes, route.getTag()) >= 0) {
                    routesToWork.put(route, new Work());
                }
            }
        }
        log.info("Added "+routesToWork.size()+" routes to worklist. ");
    }

    public Integer getFailedRuns() {
        return failedRuns;
    }

    public Integer getSuccessfulRuns() {
        return successfulRuns;
    }
    
    
}
