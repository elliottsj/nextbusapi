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
         
         // Choose the MBTA 110 - as I am near it right now....
         Route the110 = Route.find(mbtaRoutes, "110");
         RouteConfiguration rt110 = svc.getRouteConfiguration(the110);
         List<Stop> stopsOnThe110 = rt110.getStops();
         
         // Find the 3 nearest stops from me in under 1/2 mi
         // On a phone, I'd get my current GPS Locn from a Machine register.
         Geolocation here = new Geolocation(42.420391, -71.093691);
         List<Stop> closest2Me = Geolocation.sortedByClosest(stopsOnThe110, here, 3, 45);
       
         // For those three closest stops, get the current Arrival Predictions
         List<PredictionGroup> pdxns = svc.getPredictions(closest2Me);
         
         
         // Report on what we've found
         System.out.println("For Route 110 there are "+closest2Me.size()+" nearby stops");
         System.out.println(" They are are ");
         for (Stop s: closest2Me) {
             System.out.println("   "+s.getTitle()+"  "+s.getGeolocation().getDistanceInMiles(here)+" miles away");
             
         }
         for (PredictionGroup g: pdxns) {
             for (PredictionGroup.PredictionDirection d: g.getDirections()) {
                for (Prediction p : d.getPredictions()) {
                    System.out.println("   "+d.getTitle()+" "+p.getPredictedArrivalOrDepartureMinutes());
                }
             }
         }
         
         
     }
}
