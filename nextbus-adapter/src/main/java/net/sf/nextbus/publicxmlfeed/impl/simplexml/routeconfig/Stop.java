package net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class Stop {

    @Attribute
    protected String tag;

    @Attribute
    protected String title;

    @Attribute(required = false)
    protected String shortTitle;

    @Attribute
    protected double lat;

    @Attribute
    protected double lon;

    @Attribute(required = false)
    protected String stopId;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

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

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

}
