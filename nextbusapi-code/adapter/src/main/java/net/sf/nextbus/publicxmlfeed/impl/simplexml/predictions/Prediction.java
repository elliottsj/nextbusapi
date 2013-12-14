package net.sf.nextbus.publicxmlfeed.impl.simplexml.predictions;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class Prediction {

    @Attribute
    private long epochTime;

    @Attribute
    private int seconds;

    @Attribute
    private int minutes;

    @Attribute(required = false)
    private boolean isDeparture;

    @Attribute(required = false)
    private String branch;

    @Attribute
    private String dirTag;

    @Attribute(required = false)
    private String vehicle;

    @Attribute
    private String block;

    @Attribute(required = false)
    private String tripTag;

    @Attribute(required = false)
    private Boolean affectedByLayover;

    @Attribute(required = false)
    private Boolean isScheduleBased;

    @Attribute(required = false)
    private Boolean delayed;

    public long getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(long epochTime) {
        this.epochTime = epochTime;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public boolean isDeparture() {
        return isDeparture;
    }

    public void setDeparture(boolean isDeparture) {
        this.isDeparture = isDeparture;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDirTag() {
        return dirTag;
    }

    public void setDirTag(String dirTag) {
        this.dirTag = dirTag;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getTripTag() {
        return tripTag;
    }

    public void setTripTag(String tripTag) {
        this.tripTag = tripTag;
    }

    public Boolean getAffectedByLayover() {
        return affectedByLayover;
    }

    public void setAffectedByLayover(Boolean affectedByLayover) {
        this.affectedByLayover = affectedByLayover;
    }

    public Boolean getIsScheduleBased() {
        return isScheduleBased;
    }

    public void setIsScheduleBased(Boolean isScheduleBased) {
        this.isScheduleBased = isScheduleBased;
    }

    public Boolean getDelayed() {
        return delayed;
    }

    public void setDelayed(Boolean delayed) {
        this.delayed = delayed;
    }

}
