package net.sf.nextbus.publicxmlfeed.impl.simplexml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 *
 */
@Root
public class Error {

    @Attribute
    private boolean shouldRetry;

    @Text
    private String text;

}
