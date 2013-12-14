package net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * TODO: Javadoc
 */
public class RouteConfiguration {

    @Attribute
    private String tag;

    @Attribute
    private String title;

    @Attribute(required = false)
    private String shortTitle;

    @Attribute
    private String color;

    @Attribute
    private String oppositeColor;

    @Attribute
    private double latMin;

    @Attribute
    private double latMax;

    @Attribute
    private double lonMin;

    @Attribute
    private double lonMax;

    @ElementList(inline = true)
    private List<Stop> stop;

    @ElementList(inline = true)
    private List<Direction> direction;

    @ElementList(inline = true)
    private List<Path> path;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOppositeColor() {
        return oppositeColor;
    }

    public void setOppositeColor(String oppositeColor) {
        this.oppositeColor = oppositeColor;
    }

    public double getLatMin() {
        return latMin;
    }

    public void setLatMin(double latMin) {
        this.latMin = latMin;
    }

    public double getLatMax() {
        return latMax;
    }

    public void setLatMax(double latMax) {
        this.latMax = latMax;
    }

    public double getLonMin() {
        return lonMin;
    }

    public void setLonMin(double lonMin) {
        this.lonMin = lonMin;
    }

    public double getLonMax() {
        return lonMax;
    }

    public void setLonMax(double lonMax) {
        this.lonMax = lonMax;
    }

    public List<Stop> getStop() {
        return stop;
    }

    public void setStop(List<Stop> stop) {
        this.stop = stop;
    }

    public List<Direction> getDirection() {
        return direction;
    }

    public void setDirection(List<Direction> direction) {
        this.direction = direction;
    }

    public List<Path> getPath() {
        return path;
    }

    public void setPath(List<Path> path) {
        this.path = path;
    }

}
