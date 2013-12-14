package net.sf.nextbus.publicxmlfeed.impl.simplexml.predictions;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class Predictions {

    @Attribute
    private String agencyTitle;

    @Attribute
    private String routeTitle;

    @Attribute
    private String routeTag;

    @Attribute
    private String stopTitle;

    @Attribute
    private String stopTag;

    @Attribute(required = false)
    private String dirTitleBecauseNoPredictions;

    @ElementList(inline = true, empty = false, required = false)
    private List<Direction> direction;

    @ElementList(inline = true, empty = false, required = false)
    private List<Message> messages;

    public String getAgencyTitle() {
        return agencyTitle;
    }

    public void setAgencyTitle(String agencyTitle) {
        this.agencyTitle = agencyTitle;
    }

    public String getRouteTitle() {
        return routeTitle;
    }

    public void setRouteTitle(String routeTitle) {
        this.routeTitle = routeTitle;
    }

    public String getRouteTag() {
        return routeTag;
    }

    public void setRouteTag(String routeTag) {
        this.routeTag = routeTag;
    }

    public String getStopTitle() {
        return stopTitle;
    }

    public void setStopTitle(String stopTitle) {
        this.stopTitle = stopTitle;
    }

    public String getStopTag() {
        return stopTag;
    }

    public void setStopTag(String stopTag) {
        this.stopTag = stopTag;
    }

    public String getDirTitleBecauseNoPredictions() {
        return dirTitleBecauseNoPredictions;
    }

    public void setDirTitleBecauseNoPredictions(String dirTitleBecauseNoPredictions) {
        this.dirTitleBecauseNoPredictions = dirTitleBecauseNoPredictions;
    }

    public List<Direction> getDirection() {
        return direction;
    }

    public void setDirection(List<Direction> direction) {
        this.direction = direction;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
