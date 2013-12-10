package net.sf.nextbus.publicxmlfeed.domain;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.UUID;

/**
 * The Geolocation domain object needs tests to validate math and Sorts.
 * 
 * @author jrd
 */
public class GeolocationTest {

    // Some Highway Mile Markers off I-95
    // Taken with a Garmin handheld GPS
    Geolocation p1 = new Geolocation(42.10004, -71.22833);
    Geolocation p2 = new Geolocation(42.08636, -71.22228);
    Geolocation p3 = new Geolocation(42.07165, -71.22653);

    @Test
    public void testHaversineCalculation() {
        // Test distance using some real world facts
        Assert.assertEquals(p1.getDistanceInMiles(p2), 0.9947951463657195, 0.0001);
        Assert.assertEquals(p2.getDistanceInMiles(p3), 1.0394705150505739, 0.0001);
    }

    @Test
    public void basicStuff() {
        // equals - symmetry of distance operator
        Assert.assertNotSame(p1, p2);
        Assert.assertSame(p1, p1);
        Assert.assertEquals(p1.getDistanceInKm(p2), p2.getDistanceInKm(p1), 0.0001);
    }

    /**
     * Tests the Distance Sort
     */
    @Test
    public void testSort() {
        // Using a Google maps, I chose a reference point and then 4 other
        // points in increasing distance.. Feed these to the sorter and verify
        // they come out in the real world order

        // The reference point to sort against
        Geolocation refPoint = new Geolocation(42.370, -71.040);
        
        // These points are scrambled in order of distance....
        Geolocation [] testPoints = new Geolocation[]{
            new Geolocation(42.371129, -71.043410),
            new Geolocation(42.407228, -71.012429),
            new Geolocation(42.387251, -71.042003),
            new Geolocation(42.366312, -71.061885)
        };

        // Lets fabricate some fake stops using these points
        Agency mbta = new Agency("mbta", "Test Agency", "Test Agency", "Test Agency", "Test Agency");
        Route r = new Route(mbta, "route1", "Test Route", "Test Route", "Test Route");
        
        // Sort the stops by proximity from refPoint
        List<Stop> unsorted = new java.util.ArrayList<Stop>();
        for (Geolocation pt : testPoints) {
            String idVal = UUID.randomUUID().toString();
            unsorted.add(new Stop(mbta, idVal, idVal, idVal, idVal, pt, ""));
        }
       
        printDistances("unsorted", unsorted, refPoint);
        List<Stop> sorted = Geolocation.sortedByClosest(unsorted, refPoint);
        printDistances("sorted", sorted, refPoint);
        
        // Verify that the sort works....
        Assert.assertEquals("The unsorted and sort lists must be identical length ",unsorted.size(), sorted.size());
        // Check that the distances are increasing
        assertMonotoneIncreasing(sorted, refPoint);
        Assert.assertNotSame("The unsorted and sorted lists need to be different. Adjust the test data. ",sorted, unsorted);
    }

    public void printDistances(String m, List<? extends IGeocoded> arg, Geolocation refPt) {
        System.out.print(m+": ");
        for (int i = 0; i < arg.size(); i++) {
            System.out.print(arg.get(i).getGeolocation().getDistanceInMiles(refPt) + ", ");
        }
        System.out.println();
    }
    
    /**
     * Checks the Distances in a Sort - they should be monotone increasing
     * @param arg
     * @param refPt 
     */
    public void assertMonotoneIncreasing(List<? extends IGeocoded> arg, Geolocation refPt) {
        double lastDistance = -1.0;
        for (int i = 0; i < arg.size(); i++) {
            double thisDist = arg.get(i).getGeolocation().getDistanceInMiles(refPt);
            if (thisDist >= lastDistance) {
                lastDistance = thisDist;
                continue;
            }
            Assert.fail("Distance "+i+" is not monotonic increasing; value was "+thisDist+", prior entry was "+lastDistance);
        }
    }
}
