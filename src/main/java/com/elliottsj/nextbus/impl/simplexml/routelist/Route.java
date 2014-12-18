package com.elliottsj.nextbus.impl.simplexml.routelist;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * A SimpleXML-annotated class used to to deserialize each &lt;route /&gt; element in
 * the routeList XML.
 */
@Root
public class Route {

    @Attribute
    private String tag;

    @Attribute
    private String title;

    @Attribute(required = false)
    private String shortTitle;

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

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

}
