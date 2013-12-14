package net.sf.nextbus.publicxmlfeed.impl.simplexml.predictions;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * TODO: Javadoc
 */
@Root
public class Message {

    @Text
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
