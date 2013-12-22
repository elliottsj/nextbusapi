package net.sf.nextbus.publicxmlfeed.impl.simplexml.schedule;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

/**
 * TODO: Javadoc
 */
@Root(name = "tr")
public class Block {

    @Attribute
    private String blockID;

    @ElementList(inline = true)
    private List<Stop> stop;

    public String getBlockID() {
        return blockID;
    }

    public void setBlockID(String blockID) {
        this.blockID = blockID;
    }

    public List<Stop> getStop() {
        return stop;
    }

    public void setStop(List<Stop> stop) {
        this.stop = stop;
    }

    /**
     * TODO: Javadoc
     */
    @Root
    public static class Stop {

        @Attribute
        private String tag;

        @Attribute
        private long epochTime;

        @Text
        private String value;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public long getEpochTime() {
            return epochTime;
        }

        public void setEpochTime(long epochTime) {
            this.epochTime = epochTime;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
