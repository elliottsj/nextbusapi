package net.sf.nextbus.html5demo;
import net.sf.nextbus.html5demo.service.IStreamSelector;
import net.sf.nextbus.publicxmlfeed.domain.Geolocation;
import net.sf.nextbus.publicxmlfeed.domain.IGeocoded;
import java.io.Serializable;

/**
 * An EventStreamDemultiplexer Selection Strategy using Geolocation. Selects
 * for any Geolocateable within a radius of R kilometers.
 * @author jrd
 */
public class Geoselector implements IStreamSelector, Serializable {
    
    public Geolocation origin;
    public Double rangeKm;
    
    public Geoselector() {
        origin = new Geolocation(0.0,0.0);
        rangeKm = 0.0;
    }
    
    public Geoselector(Double newLat, Double newLong, Double radiusKm) {
        origin = new Geolocation(newLat, newLong);
        rangeKm = radiusKm;
    }
    
    public void setNewSearchRadius(Double newLat, Double newLong, Double radiusKm) {
        origin = new Geolocation(newLat, newLong);
        rangeKm = radiusKm;
    }
    
    @Override
    public boolean eventStreamMatches(Object arg) {
        if (arg instanceof IGeocoded) {
            IGeocoded event = (IGeocoded)arg;
            if (origin.getDistanceInKm(event.getGeolocation()) <= rangeKm) return true;
        }
        return false;
    }
    
}
