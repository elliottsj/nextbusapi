/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.nextbus.publicxmlfeed.impl.cache;
import net.sf.nextbus.publicxmlfeed.service.ServiceException;
import net.sf.nextbus.publicxmlfeed.service.INextbusService;
import net.sf.nextbus.publicxmlfeed.domain.Stop;
import net.sf.nextbus.publicxmlfeed.domain.PredictionGroup;
import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.VehicleLocation;
import net.sf.nextbus.publicxmlfeed.domain.RouteConfiguration;
import net.sf.nextbus.publicxmlfeed.domain.Schedule;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;
/**
 * A Stackable Value Object Cache - To be Finished
 * 
 * @author jrd
 * @author elliottsj
 */
public class VOCache implements INextbusService {

    private static final Logger log = Logger.getLogger(VOCache.class.getName());

    private final INextbusService backing;
    private final ICacheStore cacheStore;

    private static final long AGE_LIMIT_5MINUTES = 5*60*1000;
    private static final long AGE_LIMIT_24HOURS = 24*60*60*1000;
    
    /** Cache age limit for static data (Route, RouteConfig, Schedule) */
    private long staticDataAgeLimit = AGE_LIMIT_24HOURS;

    /** Cache age limit for dynamic data (Prediction, Location) */
    private long dynamicDataAgeLimit = AGE_LIMIT_5MINUTES;

    /**
     * Stacking constructor - allows layering over existing Service Adaptor, or even RMI proxy.
     * @param backing
     */
    public VOCache(INextbusService backing, ICacheStore cacheStore) {
        this.backing = backing;
        this.cacheStore = cacheStore;
    }

    public long getStaticDataAgeLimit() {
        return staticDataAgeLimit;
    }

    public void setStaticDataAgeLimit(long staticDataAgeLimit) {
        this.staticDataAgeLimit = staticDataAgeLimit;
    }

    public long getDynamicDataAgeLimit() {
        return dynamicDataAgeLimit;
    }

    public void setDynamicDataAgeLimit(long dynamicDataAgeLimit) {
        this.dynamicDataAgeLimit = dynamicDataAgeLimit;
    }

    public List<Agency> getAgencies() throws ServiceException {
        if (!cacheStore.isAgenciesCached() || cacheStore.getAgenciesAge() > staticDataAgeLimit) {
            List<Agency> backingResult = backing.getAgencies();
            cacheStore.putAgencies(backingResult);
            return backingResult;
        }
        return cacheStore.getAgencies();
    }

    public Agency getAgency(String id) throws ServiceException {
        // if cacheMissOrTooOld(...) refreshCache(...); 
        // return cache.get(...); 
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<PredictionGroup> getPredictions(Stop s) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<PredictionGroup> getPredictions(Route route, Collection<Stop> stops) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<PredictionGroup> getPredictions(Map<Route, List<Stop>> stops) throws ServiceException {
        return null;
    }

    public PredictionGroup getPredictions(Route r, Stop s) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RouteConfiguration getRouteConfiguration(Route route) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Route> getRoutes(Agency agency) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<VehicleLocation> getVehicleLocations(Route route, long deltaT) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Schedule> getSchedule(Route route) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
