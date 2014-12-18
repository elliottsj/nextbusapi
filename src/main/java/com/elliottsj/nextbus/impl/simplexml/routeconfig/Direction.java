package com.elliottsj.nextbus.impl.simplexml.routeconfig;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * A SimpleXML-annotated class used to to deserialize each &lt;direction /&gt; element in
 * in the routeConfig XML.
 */
@Root
public class Direction {

    @Attribute
    private String tag;

    @Attribute
    private String title;

    @Attribute(required = false)
    private String name;

    @Attribute
    private Boolean useForUI;

    @Attribute(required = false)
    private String branch;

    @ElementList(inline = true)
    private List<Stop> stops;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUseForUI() {
        return useForUI;
    }

    public void setUseForUI(Boolean useForUI) {
        this.useForUI = useForUI;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    @Root
    public static class Stop {

        @Attribute
        private String tag;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

    }

}
