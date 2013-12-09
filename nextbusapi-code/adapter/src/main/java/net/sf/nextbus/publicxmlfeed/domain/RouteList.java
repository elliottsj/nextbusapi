package net.sf.nextbus.publicxmlfeed.domain;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 *
 */
public class RouteList extends NextBusListObject<Route> {

    @ElementList(inline = true)
    private List<Route> list;

    @Override
    public List<Route> getList() {
        return list;
    }

}
