package net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig;

import net.sf.nextbus.publicxmlfeed.impl.simplexml.Body;
import org.simpleframework.xml.ElementList;

import java.util.List;

public class RouteConfigBody extends Body {

    @ElementList(inline = true, entry = "route")
    private List<RouteConfiguration> routes;

    public List<RouteConfiguration> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteConfiguration> routes) {
        this.routes = routes;
    }

}
