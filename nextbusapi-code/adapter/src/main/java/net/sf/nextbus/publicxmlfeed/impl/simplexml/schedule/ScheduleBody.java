package net.sf.nextbus.publicxmlfeed.impl.simplexml.schedule;

import net.sf.nextbus.publicxmlfeed.impl.simplexml.Body;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * TODO: Javadoc
 */
public class ScheduleBody extends Body {

    @ElementList(inline = true)
    protected List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

}
