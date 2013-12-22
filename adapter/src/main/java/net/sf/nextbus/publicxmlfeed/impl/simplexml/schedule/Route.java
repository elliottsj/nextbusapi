package net.sf.nextbus.publicxmlfeed.impl.simplexml.schedule;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * TODO: Javadoc
 */
@Root
public class Route {

    @Attribute
    private String tag;

    @Attribute
    private String title;

    @Attribute
    private String scheduleClass;

    @Attribute
    private String serviceClass;

    @Attribute
    private String direction;

    @ElementList
    private List<Stop> header;

    @ElementList(inline = true)
    private List<Block> blocks;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScheduleClass() {
        return scheduleClass;
    }

    public void setScheduleClass(String scheduleClass) {
        this.scheduleClass = scheduleClass;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<Stop> getHeader() {
        return header;
    }

    public void setHeader(List<Stop> header) {
        this.header = header;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

}
