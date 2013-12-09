package net.sf.nextbus.publicxmlfeed.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 *
 */
@Root(name = "body")
public abstract class NextBusListObject<T> extends NextBusValueObject {

    private static final long serialVersionUID = -8536583824071639484L;

    @Attribute
    private String copyright;

    @Element(required = false)
    protected Error error;

    public String getCopyright() {
        return copyright;
    }

    public abstract List<T> getList();

    /**
     * Gets the error message for when the response XML contains an error element
     *
     * @return the error message
     */
    public final Error getError() {
        return error;
    }

}
