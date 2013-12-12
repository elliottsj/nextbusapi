package net.sf.nextbus.publicxmlfeed.domain;

import org.simpleframework.xml.ElementList;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * TODO: javadoc
 */
public class RouteConfigurationList extends NextBusListObject<RouteConfiguration> {

    private static final long serialVersionUID = 3991548209357245300L;

    @ElementList(inline = true)
    private List<RouteConfiguration> list;

    @Override
    public List<RouteConfiguration> getList() {
        return list;
    }

    /**
     * Gets the first route configuration if it exists
     *
     * @return the first route configuration
     * @throws NoSuchElementException when no route configuration exists
     */
    public RouteConfiguration getRouteConfiguration() {
        try {
            return list.get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("This RouteConfigurationList does not contain a route configuration.");
        }
    }

}
