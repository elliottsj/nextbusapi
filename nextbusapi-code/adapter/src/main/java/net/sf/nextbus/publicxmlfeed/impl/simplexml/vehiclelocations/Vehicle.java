package net.sf.nextbus.publicxmlfeed.impl.simplexml.vehiclelocations;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * TODO: Javadoc
 */
@Root
public class Vehicle {

    @Attribute
    private String id;

    @Attribute(required = false)
    private String routeTag;

    @Attribute(required = false)
    private String dirTag;

    @Attribute
    private double lat;

    @Attribute
    private double lon;

    @Attribute
    private long secsSinceReport;

    @Attribute
    private boolean predictable;

    @Attribute
    private int heading;

    @Attribute(required = false)
    private double speedKmHr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRouteTag() {
        return routeTag;
    }

    public void setRouteTag(String routeTag) {
        this.routeTag = routeTag;
    }

    public String getDirTag() {
        return dirTag;
    }

    public void setDirTag(String dirTag) {
        this.dirTag = dirTag;
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

    public long getSecsSinceReport() {
        return secsSinceReport;
    }

    public void setSecsSinceReport(long secsSinceReport) {
        this.secsSinceReport = secsSinceReport;
    }

    public boolean isPredictable() {
        return predictable;
    }

    public void setPredictable(boolean predictable) {
        this.predictable = predictable;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public double getSpeedKmHr() {
        return speedKmHr;
    }

    public void setSpeedKmHr(double speedKmHr) {
        this.speedKmHr = speedKmHr;
    }

}
