package com.elliottsj.nextbus.impl.simplexml.messages;

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

    @ElementList(inline = true)
    private List<Message> messages;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
