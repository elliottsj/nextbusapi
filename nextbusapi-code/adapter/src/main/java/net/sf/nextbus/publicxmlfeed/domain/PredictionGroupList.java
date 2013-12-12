package net.sf.nextbus.publicxmlfeed.domain;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * TODO: javadoc
 */
public class PredictionGroupList extends NextBusListObject<PredictionGroup> {

    @ElementList(inline = true, name = "predictions")
    private List<PredictionGroup> list;

    @Override
    public List<PredictionGroup> getList() {
        return list;
    }

}
