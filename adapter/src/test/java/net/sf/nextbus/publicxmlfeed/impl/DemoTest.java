/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.nextbus.publicxmlfeed.impl;
import net.sf.nextbus.publicxmlfeed.service.IService;
import net.sf.nextbus.publicxmlfeed.domain.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

import org.junit.Before;
/**
 * This simple unit test demonstrates full use of the API to retrieve bus
 * activity close to the users' location.
 * 
 * @author jrd
 */
public class DemoTest {
     IService svc;
     
     @Before
    public void setup() throws Exception {
         //svc = remoteBinding();
         svc=new SimplestServiceAdapter();
    }
     
     @Test
     public void testDemoCode() {
         
         // Choose the MBTA 
         Agency mbta = svc.getAgency("mbta");
         List<Route> mbtaRoutes  = svc.getRoutes(mbta);
         
         // Choose the MBTA 100 Bus - as I am near it right now....
         Route route = Route.find(mbtaRoutes, "110");
         RouteConfiguration routeConf = svc.getRouteConfiguration(route);
         List<Stop> stops = routeConf.getStops();
         
         // Find the 3 nearest stops from me in under 1/2 mi
         // On a phone, I'd get my current GPS Locn from a Machine register.
         Geolocation here = new Geolocation(42.42121, -71.09336);
         List<Stop> closest2Me = Geolocation.sortedByClosest(stops, here, 10, 45);
       
         // For those three closest stops, get the current Arrival Predictions
         List<PredictionGroup> pdxns = svc.getPredictions(closest2Me);
         
         
         // Report on what we've found
         System.out.println("For Route 101 there are "+closest2Me.size()+" nearby stops");
         System.out.println(" The three closest stop to you are: ");
         for (Stop s: closest2Me) {
             System.out.println("   "+s.getTitle()+"  "+s.getGeolocation().getDistanceInMiles(here)+" miles away");
             
         }
         System.out.println("Arrival predictions follow for your closest stops");
         System.out.println("*************************************************");
         for (PredictionGroup g: pdxns) {
             for (PredictionGroup.PredictionDirection pd: g.getDirections()) {
                for (Prediction p : pd.getPredictions()) {
                    Direction d = routeConf.getDirectionById(p.getDirectionId());
                    System.out.printf("%s %s   %s %tk:%<tM \n",d.getTitle(), d.getName(), p.getPredictedArrivalOrDepartureMinutes(), p.getPredictedArrivalOrDepartureTimeUTC());
                }
             }
         }
         System.out.println("Vehicles on route");
         System.out.println("*****************");
         for (VehicleLocation v: svc.getVehicleLocations(route, 0)) {
             Direction d = routeConf.getDirectionById(v.getDirectionId());
             System.out.printf("%s %s %.1f miles away from ,e.\n",v.getVehicle(), d, v.getGeolocation().getDistanceInMiles(here));
         }
         
     }
}
