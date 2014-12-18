package com.elliottsj.nextbus.impl.simplexml.schedule;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * TODO: Javadoc
 */
@Root
public class Stop {

    @Attribute
    private String tag;

    @Text
    private String value;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
