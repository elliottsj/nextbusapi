/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.nextbus.publicxmlfeed.domain;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jrd
 */
public class GeocodeTest {

    // Some Highway Mile Markers off I-95
    // Taken with a Garmin handheld GPS
    Geolocation p1 = new Geolocation(42.10004, -71.22833);
    Geolocation p2 = new Geolocation(42.08636, -71.22228);
    Geolocation p3 = new Geolocation(42.07165, -71.22653);

    @Test
    public void testHaversineCalculation() {
        Assert.assertEquals(p1.getDistanceInMiles(p2), 0.9947951463657195);
        Assert.assertEquals(p2.getDistanceInMiles(p3), 1.0394705150505739);
    }
    
    @Test
    public void basicStuff() {
        Assert.assertNotSame(p1, p2);
        Assert.assertSame(p1, p1);
        Assert.assertEquals(p1.getDistanceInKm(p2), p2.getDistanceInKm(p1));
    }
}
