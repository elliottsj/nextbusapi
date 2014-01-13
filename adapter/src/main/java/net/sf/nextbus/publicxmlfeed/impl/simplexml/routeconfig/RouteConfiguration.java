package net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * A SimpleXML-annotated class used to to deserialize each &lt;route /&gt; element in
 * the routeConfig XML.
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
    private List<Stop> stops;

    @ElementList(inline = true)
    private List<Direction> directions;

    @ElementList(inline = true)
    private List<Path> paths;

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

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

}
