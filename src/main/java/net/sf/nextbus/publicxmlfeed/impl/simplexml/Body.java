package net.sf.nextbus.publicxmlfeed.impl.simplexml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * A SimpleXML-annotated base class used to deserialize XML provided by NextBus.
 */
@Root
public abstract class Body {

    @Attribute
    private String copyright;

    @Element(name = "Error", required = false)
    private Error error;

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String value) {
        this.copyright = value;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error value) {
        this.error = value;
    }

}
