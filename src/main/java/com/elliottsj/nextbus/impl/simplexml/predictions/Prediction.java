package com.elliottsj.nextbus.impl.simplexml.predictions;

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
    private boolean affectedByLayover;

    @Attribute(required = false)
    private boolean isScheduleBased;

    @Attribute(required = false)
    private boolean delayed;

    @Attribute(required = false)
    private String slowness;

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

    public boolean isAffectedByLayover() {
        return affectedByLayover;
    }

    public void setAffectedByLayover(boolean affectedByLayover) {
        this.affectedByLayover = affectedByLayover;
    }

    public boolean isScheduleBased() {
        return isScheduleBased;
    }

    public void setScheduleBased(boolean isScheduleBased) {
        this.isScheduleBased = isScheduleBased;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    public String getSlowness() {
        return slowness;
    }

    public void setSlowness(String slowness) {
        this.slowness = slowness;
    }

}
