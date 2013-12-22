package net.sf.nextbus.publicxmlfeed.impl.simplexml.messages;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * TODO: Javadoc
 */
@Root
public class Interval {

    @Attribute
    private int startDay;

    @Attribute
    private int startTime;

    @Attribute
    private int endDay;

    @Attribute
    private int endTime;

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

}
