package net.sf.nextbus.publicxmlfeed.domain;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * A SimpleXML-annotated POJO for a NextBus agency list retrieved from URL:
 *
 * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList">http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList</a>
 */
public class AgencyList extends NextBusListObject<Agency> {

    @ElementList(inline = true)
    private List<Agency> list;

    @Override
    public List<Agency> getList() {
        return list;
    }

}
