package net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * A SimpleXML-annotated class used to to deserialize each &lt;point /&gt; element in
 * a &lt;path /&gt; in the routeConfig XML.
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
