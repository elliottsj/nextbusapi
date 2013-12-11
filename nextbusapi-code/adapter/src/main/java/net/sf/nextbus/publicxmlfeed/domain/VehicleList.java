package net.sf.nextbus.publicxmlfeed.domain;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 *
 */
public class VehicleList extends NextBusListObject<Vehicle> {

    @ElementList(inline = true)
    private List<Vehicle> list;

    @Override
    public List<Vehicle> getList() {
        return list;
    }

}
