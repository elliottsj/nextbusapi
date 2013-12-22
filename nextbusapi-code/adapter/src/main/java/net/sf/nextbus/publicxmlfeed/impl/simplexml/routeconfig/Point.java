package net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * TODO: Javadoc
 */
@Root
public class Point {

    @Attribute
    private double lat;

    @Attribute
    private double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

}
