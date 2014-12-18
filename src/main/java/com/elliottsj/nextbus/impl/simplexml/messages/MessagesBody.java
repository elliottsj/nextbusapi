package com.elliottsj.nextbus.impl.simplexml.messages;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * TODO: Javadoc
 */
public class MessagesBody {

    @ElementList(inline = true)
    protected List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

}
