package com.elliottsj.nextbus.impl.simplexml.routelist;

import com.elliottsj.nextbus.impl.simplexml.Body;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * A SimpleXML-annotated class used to to deserialize XML returned by a request to:
 *
 * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a=">
 *     http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a=&lt;agency_tag&gt;
 * </a>
 */
public class RouteListBody extends Body {

    @ElementList(inline = true, required = false)
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

}
