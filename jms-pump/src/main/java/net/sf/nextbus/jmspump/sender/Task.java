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
import net.sf.nextbus.publicxmlfeed.domain.Stop;
import net.sf.nextbus.publicxmlfeed.domain.RouteConfiguration;
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
     * The freshness limit in milliseconds, defaults to 5 min for VehLocn, 10 min for Predictions
     */
    private long refreshIntervalVehicleLocations = 60 * 1000L;
    private long refreshIntervalPredictions = 10 * 60 * 1000L;
    private boolean enableVehicleLocations = false;
    private boolean enablePredictions = true;
    
    /**
     * Max number of NextBus HTTP calls to make per Scheduler pass
     */
    private Integer maxNextbusCallsPerExecution = 5;
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
    private Set<Route> routes = new HashSet<Route>();
    private Set<TaskWorker> work = new HashSet<TaskWorker>();
    private int successfulRuns;
    private int failedRuns;
    
    /** Inhibits the execute() method from doing work */
    private boolean paused = true;


    /**
     * Ctor to configure task to poll all routes for a given agency
     *
     * @param agency
     */
    public Task(INextbusService svc, String agency) {
        nextbus = svc;
        Agency a = nextbus.getAgency(agency);
        agencies.add(a);
        setupRoutes(a, null);
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
            setupRoutes(a, null);
        } else {
            String[] rs = routes.trim().split(",");
            setupRoutes(a, rs);
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
     * Post-construct init ; mandatory
     */
    public void init() {
        if (enablePredictions) setupStopPredictionTasks();
        if (enableVehicleLocations) setupVehicleLocationTasks();
        
        // what if there is no work to do?
        if (work.size() == 0) {
            log.error("*** There are no available routes to work. Terminating.");
            paused=true;
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

     * @return A collection of POJOs to insert into a Spring Integration
     * Channel.
     */
    public List execute() {
        int nextbusCalls = 0;
        List workproducts = new ArrayList();
        if (paused) {
            log.info("Task is paused...");
            return workproducts;
        }
        
        int done = 0;
        for (TaskWorker workItem : this.work) {
            // throttle
            if (nextbusCalls+1 > maxNextbusCallsPerExecution) {
                log.debug("Limiting NextBus XML RPCs on this pass to maintain service level agreement upper threshhold.");
                break;
            }
            
            if (workItem.isOld()) {
                workproducts.addAll(workItem.execute());
                done++;
                nextbusCalls++;
            } else {
                log.trace("skipping; not time to do "+work);
            }
        }
        if (done > 0) {
            log.info("obtained refresh from NextBus for " + done + " work items with a total of " + workproducts.size() + " objects.");
        }
        return workproducts;
    }

    
    private void setupVehicleLocationTasks() {
        for (Route route: routes) {
            work.add(new VehicleLocationTaskWorker(nextbus,route,refreshIntervalVehicleLocations));
        }
        log.info("Setup Vehicle Location Tasks on "+routes.size()+" routes.");
    }
    private void setupStopPredictionTasks() {
        // for each Route, get the stops list
        int totalStops = 0;
        for (Route route: routes) {
            RouteConfiguration routeConfig = nextbus.getRouteConfiguration(route);
            List<Stop> stops = routeConfig.getStops();
            work.add(new PredictionTaskWorker(nextbus, stops, refreshIntervalPredictions));
            totalStops += stops.size();
        }
        log.info("Setup Prediction Tasks on "+routes.size()+" routes with "+totalStops+" stops.");
    }
    /**
     * Determine the set of Routes to work for an agency
     *
     * @param agency
     * @param routes
     */
    private void setupRoutes(Agency agency, String[] routes) {
        // get all the routes for this agency
        List<Route> rs = nextbus.getRoutes(agency);

        // Configured for all routes?  That's the common use case.
        //   Just create a work element for each discovered route
        if (routes == null || routes.length == 0) {
            System.out.print("Using all routes for agency: "+agency.getId()+", total of "+rs.size()+" :");
            for (Route r : rs) {
                this.routes.add(r);
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
                this.routes.add(route);
            } else {
                log.warn("route " + r + " not found is authoritative list of routes for agency " + agency);
            }
        }
        
        
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

    /*
     * Sets max number RPCs per pass of the execute() method - for bandwidth levelling
     */
    public void setMaxNextbusCallsPerExecution(Integer arg) {
        if (arg<=0) return;
        this.maxNextbusCallsPerExecution = arg;
    }

    /**
     * Sweep Predictions
     * @param enablePredictions 
     */
    public void setEnablePredictions(boolean arg) {
        this.enablePredictions = arg;
    }

    /**
     * Sweep Locations
     * @param enableVehicleLocations 
     */
    public void setEnableVehicleLocations(boolean arg) {
        this.enableVehicleLocations = arg;
    }

    /**
     * Sets the holding time of Predictions
     * @param refreshIntervalPredictions 
     */
    public void setRefreshIntervalPredictions(Long arg) {
        this.refreshIntervalPredictions = arg;
    }

    /**
     * sets the holding time of Locations
     * @param refreshIntervalVehicleLocations 
     */
    public void setRefreshIntervalVehicleLocations(Long arg) {
        this.refreshIntervalVehicleLocations = arg;
    }
    
    
}
