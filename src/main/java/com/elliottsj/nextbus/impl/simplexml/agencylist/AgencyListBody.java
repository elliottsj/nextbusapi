package com.elliottsj.nextbus.impl.simplexml.agencylist;

import com.elliottsj.nextbus.impl.simplexml.Body;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * A SimpleXML-annotated class used to to deserialize XML returned by a request to:
 *
 * <a href="http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList">
 *     http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList
 * </a>
 */
public class AgencyListBody extends Body {

    @ElementList(inline = true)
    private List<Agency> agencies;

    public List<Agency> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<Agency> agencies) {
        this.agencies = agencies;
    }

}
