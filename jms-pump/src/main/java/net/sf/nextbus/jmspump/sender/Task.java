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

import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.VehicleLocation;
import net.sf.nextbus.publicxmlfeed.service.INextbusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * <p> Worker Task is responsible for preparing and gathering items to stream to
 * JMS. In the Spring Integration context, it's just a POJO with a method that
 * is called and returns a Collection of stuff that Spring Integration treats as
 * Message traffic.
 *
 * This worker task has some more elaborate internal housekeeping. It maintains
 * a work list of items with timestamps to insure we don't excessively call
 * Nextbus every time this Task is invoked by Spring Integration, which might be
 * quite frequently.
 *
 * Further, the worker task maintains a set of Transit Routes to work over...
 * It's capable of configuring it's own work list by discovering Routes directly
 * from NextBus. </p>
 *
 * @author jrd
 */
public class Task {

    final Logger log = LoggerFactory.getLogger(Task.class);
    /**
     * The freshness limit in milliseconds, defaults to 60 seconds
     */
    private Long refreshInterval = 60 * 1000L;
    /**
     * Nextbus service adapter
     */
    private INextbusService nextbus;
    /**
     * The Nextbus agencies to work on.
     */
    private Set<Agency> agencies = new HashSet<Agency>();
    /**
     * The Work schedule of Bus Routes
     */
    private Set<Work> routesToWork = new HashSet<Work>();
    private int successfulRuns;
    private int failedRuns;
    
    private boolean paused = true;

    public void setRefreshInterval(long arg) {
        refreshInterval = arg;
    }

    /**
     * Tracks work done against a Route by the sweep Task. Timestamp is used
     * to sense when update/refresh needs to be done.
     */
    private class Work {

        Work(Route r) {
            route = r;
        }
        Route route;
        long lastTime;
        int errors;
        int success;

        boolean isOld() {
            return System.currentTimeMillis() - lastTime >= refreshInterval;
        }
    }

    /**
     * Ctor to configure task to poll all routes for a given agency
     *
     * @param agency
     */
    public Task(INextbusService svc, String agency) {
        nextbus = svc;
        Agency a = nextbus.getAgency(agency);
        agencies.add(a);
        configureRoutes(a, null);
    }

    /**
     * Ctor to configure worker to poll a selected set of routes for a given
     * agency
     *
     * @param agency the agency to work, i.e. MBTA
     * @param routes the specific routes to work as a CSV string of Route tags,
     * i.e. "110, 111"
     */
    public Task(INextbusService svc, String agency, String routes) {
        nextbus = svc;
        Agency a = nextbus.getAgency(agency);
        agencies.add(a);
        if (routes == null || routes.isEmpty()) {
            configureRoutes(a, null);
        } else {
            String[] rs = routes.trim().split(",");
            configureRoutes(a, rs);
        }
    }

    /**
     * Ctor to configure worker to poll all routes for a group of agencies -
     * Warning this will create a substantial burder on the NextBus Service.
     *
     * @param agcs Agencies to poll, as a CSV String i.e. "brooklyn,
     * staten-island,"
     */
    public Task(String agcs) {
        String[] _agencies = agcs.split(",");
        for (String a : _agencies) {
            Agency agency = nextbus.getAgency(a);
            agencies.add(agency);
        }
    }

    /**
     * Task Executor, should be called periodically as a Scheduled Task by
     * Spring Integration. The executor sweeps the Work table, finds things that
     * are stale, and then tries to refresh them by delegating to an appropriate
     * handler method that relies on the NextBus adapter. If errors occur. they
     * are logged, silently counted - but do not interfere with the harvesting
     * job. Thus, this message pumper is safe against transient nextbus
     * failures.
     */
    /**
     *
     * @return A collection of POJOs to insert into a Spring Integration
     * Channel.
     */
    public List execute() {
        List workproducts = new ArrayList();
        if (paused) {
            log.info("Task is paused...");
            return workproducts;
        }
        
        
        int done = 0;
        for (Work work : routesToWork) {
            if (work.isOld()) {
                doVehicleLocations(workproducts, work);
                done++;
            } else {
                log.trace("skipping "+work.route+ " ; still recent. ");
            }
        }
        if (done > 0) {
            log.info("obtained refresh from NextBus for " + done + " routes with a total of " + workproducts.size() + " vehicles in service.");
        }
        return workproducts;
    }

    /**
     * Refreshes a work item for a Routes VehicleLocations
     *
     * @param work item to do
     * @param r
     */
    private void doVehicleLocations(List workproducts, Work work) {
        try {
            List<VehicleLocation> vl = nextbus.getVehicleLocations(work.route, 0);

            work.lastTime = System.currentTimeMillis();
            work.success++;
            // Cant have a list of containing nested lists.. Unpack the 
            // VehicleLocations and add to the work done list
            for (VehicleLocation v : vl) {
                workproducts.add(v);
            }
            successfulRuns++;
            log.debug("got update for " + work.route + " with " + vl.size() + " vehicle locations.");
        } catch (net.sf.nextbus.publicxmlfeed.service.ServiceException se) {
            work.errors++;
            log.warn("Fault while obtaining update for " + work.route, se);
            failedRuns++;
        }
    }

    /**
     * Configure the worker schedule for a set of routes, or all routes, for the
     * given agency.
     *
     * @param agency
     * @param routes
     */
    private void configureRoutes(Agency agency, String[] routes) {
        // get all the routes for this agency
        List<Route> rs = nextbus.getRoutes(agency);

        // Configured for all routes?  That's the common use case.
        //   Just create a work element for each discovered route
        if (routes == null || routes.length == 0) {
            for (Route r : rs) {
                routesToWork.add(new Work(r));
                System.out.print(r.getTag()+",");
            }
            System.out.println();
            return;
        }

        // Configured for just a few explicitly named routes?  
        // Validate the method arguments against the authoritative set of routes from the Agency.
        for (String r : routes) {
            Route route = null;
            for (Route r1 : rs) {
                if (r1.getTag().equals(r)) {
                    route = r1;
                    break;
                }
            }
            if (route != null) {
                routesToWork.add(new Work(route));
            } else {
                log.warn("route " + route + " not found is authoritative list of routes for agency " + agency);
            }
        }
        log.info("Added " + routesToWork.size() + " routes to worklist. ");
    }

    public Integer getFailedRuns() {
        return failedRuns;
    }

    public Integer getSuccessfulRuns() {
        return successfulRuns;
    }
    
    /**
     * Pauses the sweeper task ; needed to support daemonization of the Main program.
     * @param arg 
     */
    public void setPaused(boolean arg) { 
        paused=arg;
    }
}
