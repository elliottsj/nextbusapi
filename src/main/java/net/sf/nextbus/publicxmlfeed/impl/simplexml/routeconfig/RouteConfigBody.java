package net.sf.nextbus.publicxmlfeed.impl.simplexml.routeconfig;

import net.sf.nextbus.publicxmlfeed.impl.simplexml.Body;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * A SimpleXML-annotated class used to to deserialize XML returned by a request to:
 *
 * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=&r=">
 *     http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=&lt;agency_tag&gt;&r=&lt;route_tag&gt;
 * </a>
 */
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
