package com.elliottsj.nextbus.impl.simplexml.vehiclelocations;

import com.elliottsj.nextbus.impl.simplexml.Body;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * TODO: Javadoc
 */
public class VehicleLocationsBody extends Body {

    @ElementList(inline = true, empty = false, required = false)
    private List<Vehicle> vehicles;

    @Element
    private LastTime lastTime;

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public LastTime getLastTime() {
        return lastTime;
    }

    public void setLastTime(LastTime lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * TODO: Javadoc
     */
    @Root
    public static class LastTime {

        @Attribute
        protected long time;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

    }

}
