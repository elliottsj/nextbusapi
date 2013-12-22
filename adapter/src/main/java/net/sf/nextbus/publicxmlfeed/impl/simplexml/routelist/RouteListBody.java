package net.sf.nextbus.publicxmlfeed.impl.simplexml.routelist;

import net.sf.nextbus.publicxmlfeed.impl.simplexml.Body;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * TODO: Javadoc
 */
public class RouteListBody extends Body {

    @ElementList(inline = true)
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

}
