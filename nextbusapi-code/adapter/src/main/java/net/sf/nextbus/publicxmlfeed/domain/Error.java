package net.sf.nextbus.publicxmlfeed.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 *
 */
@Root
public class Error extends NextBusValueObject {

    @Attribute
    private boolean shouldRetry;

    @Text
    private String value;

    public boolean isShouldRetry() {
        return shouldRetry;
    }

    public String getValue() {
        return value;
    }

}
