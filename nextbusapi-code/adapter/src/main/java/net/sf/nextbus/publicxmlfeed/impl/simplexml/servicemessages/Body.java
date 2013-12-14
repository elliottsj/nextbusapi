package net.sf.nextbus.publicxmlfeed.impl.simplexml.servicemessages;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * TODO: Javadoc
 */
public class Body {

    @ElementList(inline = true)
    protected List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

}
