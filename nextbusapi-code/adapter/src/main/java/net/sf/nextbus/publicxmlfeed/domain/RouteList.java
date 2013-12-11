package net.sf.nextbus.publicxmlfeed.domain;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 *
 */
public class RouteList extends NextBusListObject<Route> {

    private static final long serialVersionUID = -8606928572699520200L;

    @ElementList(inline = true)
    private List<Route> list;

    @Override
    public List<Route> getList() {
        return list;
    }

    /**
     * Sets the agency for all routes in this route list.
     *
     * @param agency the agency to set
     */
    public void setAgency(Agency agency) {
        for (Route route : getList())
            route.setAgency(agency);
    }

}
