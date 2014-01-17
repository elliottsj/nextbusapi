package net.sf.nextbus.publicxmlfeed.impl;

import net.sf.nextbus.publicxmlfeed.domain.*;
import net.sf.nextbus.publicxmlfeed.service.INextbusService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * This simple unit test demonstrates full use of the API to retrieve bus
 * activity close to the users' location.
 *
 * @author jrd
 */
public class DemoTest {

    INextbusService svc;

    private Agency mbta;
    private Route route110, route111;
    private List<Stop> stops110, stops111;

    @Before
    public void setup() throws Exception {
        //svc = remoteBinding();
        svc = new SimplestNextbusServiceAdapter();
        mbta = svc.getAgency("mbta");
        List<Route> mbtaRoutes = svc.getRoutes(mbta);
        route110 = Route.find(mbtaRoutes, "110");
        route111 = Route.find(mbtaRoutes, "111");
        RouteConfiguration routeConf110 = svc.getRouteConfiguration(route110);
        RouteConfiguration routeConf111 = svc.getRouteConfiguration(route111);
        stops110 = routeConf110.getStops();
        stops111 = routeConf111.getStops();
    }

    @Test(expected=IllegalArgumentException.class) 
    public void testStopsById(){
        Stop stop8310 = Stop.find(stops110, "8310");
    }
    
    /**
     * Tests for the heavily overloaded predictions interface
     */
    @Test
    public void testPredictions() {
        // Choose the MBTA 


        // Select a well known stop that spans multiple routes
        Stop stop8310 = Stop.find(stops111, "8310");

        // get predictions for a Single Stop but all Routes covering it
        List<PredictionGroup> ps8310 = svc.getPredictions(stop8310);
        Assert.assertTrue("Stop 8310 MBTA should span more than one route ",ps8310.size()>1);
        
        // get predictions for an entire route
        List<PredictionGroup> pdxnRoute111 = svc.getPredictions(route111, stops111);
        Assert.assertTrue("", pdxnRoute111.size()>1);
        
        // get predictions for a single Route/Stop combo
        PredictionGroup pg1 = svc.getPredictions(route111, stop8310);
        Assert.assertTrue(pg1.getAvailablePredictions()>0);
       
        
    }

    /**
     * Tests a a walkthrough scenario with the API for a typical use case
     */
    @Test
    public void testDemoCode() {

        // Choose the MBTA 
        List<Route> mbtaRoutes = svc.getRoutes(mbta);

        // Choose the MBTA 100 Bus - as I am near it right now....


        // Find the 3 nearest stops from me in under 1/2 mi
        // On a phone, I'd get my current GPS Locn from a Machine register.
        Geolocation here = new Geolocation(42.42121, -71.09336);
        List<Stop> closest2Me = Geolocation.sortedByClosest(stops110, here, 10, 45);

        // For those three closest stops, get the current Arrival Predictions
        List<PredictionGroup> pdxns = svc.getPredictions(route110, closest2Me);

        // Report on what we've found
        System.out.println("For Route 101 there are " + closest2Me.size() + " nearby stops");
        System.out.println(" The three closest stop to you are: ");
        for (Stop s : closest2Me) {
            System.out.println("   " + s.getTitle() + "  " + s.getGeolocation().getDistanceInMiles(here) + " miles away");

        }
        System.out.println("Arrival predictions follow for your closest stops");
        System.out.println("*************************************************");
        for (PredictionGroup g : pdxns) {
            for (PredictionGroup.PredictionDirection pd : g.getDirections()) {
                for (Prediction p : pd.getPredictions()) {
                    System.out.printf("%s   %s %tk:%<tM \n", pd.getTitle(), p.getPredictedArrivalOrDepartureMinutes(), p.getPredictedArrivalOrDepartureTimeUTC());
                }
            }
        }
        System.out.println("Vehicles on route");
        System.out.println("*****************");
        RouteConfiguration routeConf = svc.getRouteConfiguration(route110);
        for (VehicleLocation v : svc.getVehicleLocations(route110, 0)) {
            System.out.printf("%s %s %.1f miles away from ,e.\n", v.getVehicle(), v.getDirectionId(), v.getGeolocation().getDistanceInMiles(here));
        }

    }
    
    // disable test for now...
    public void testRouteConfig() {
        List<Route> mbtaRoutes = svc.getRoutes(mbta);
        long start = System.currentTimeMillis();
        Map<Route, RouteConfiguration> totalRouteConfig = new HashMap<Route, RouteConfiguration>();
        Set<Stop> allstops = new HashSet<Stop>();
        for (Route route : mbtaRoutes) {
            RouteConfiguration rc = svc.getRouteConfiguration(route);
            totalRouteConfig.put(route, rc);
            allstops.addAll(rc.getStops());
            
        }
        long done = System.currentTimeMillis();
        System.out.println("Total Route Config took "+(done-start)+" milliseconds");
        System.out.println("There are "+allstops.size()+" stops.");
        System.out.println(allstops);
    }
}
