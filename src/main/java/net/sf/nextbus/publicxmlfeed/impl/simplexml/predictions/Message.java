package net.sf.nextbus.publicxmlfeed.impl.simplexml.predictions;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * TODO: Javadoc
 */
@Root
public class Message {

    @Attribute
    private String text;

    @Attribute(required = false)
    private String priority;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
