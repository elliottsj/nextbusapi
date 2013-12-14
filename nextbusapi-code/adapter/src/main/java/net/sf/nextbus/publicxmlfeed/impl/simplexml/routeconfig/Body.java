package net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig;

import org.simpleframework.xml.ElementList;

import java.util.List;

public class Body  {

    @ElementList(inline = true, name = "route")
    private List<RouteConfiguration> routes;

    public List<RouteConfiguration> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteConfiguration> routes) {
        this.routes = routes;
    }

}
