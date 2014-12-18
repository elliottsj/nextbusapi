package com.elliottsj.nextbus.impl.simplexml.routeconfig;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * TODO: Javadoc
 */
@Root
public class Path {

    @ElementList(inline = true)
    protected List<Point> points;

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

}
