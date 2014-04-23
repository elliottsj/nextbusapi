package net.sf.nextbus.publicxmlfeed.impl.simplexml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 *
 */
@Root(name = "Error")
public class Error {

    @Attribute
    private boolean shouldRetry;

    @Text
    private String value;

    public boolean isShouldRetry() {
        return shouldRetry;
    }

    public void setShouldRetry(boolean shouldRetry) {
        this.shouldRetry = shouldRetry;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
