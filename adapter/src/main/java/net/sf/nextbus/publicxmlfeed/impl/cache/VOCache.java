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
 */
public class VOCache implements INextbusService {

    
    private static final Logger log = Logger.getLogger(VOCache.class.getName());
    private final INextbusService underlyingSvc;
    
    /** The default Cache ageout limit is 5 minutes */
    private static final long defaultAgeLimit5minutes = 5*60*1000;
    
    /** Cache age limit for Static data (Routelists, RouteConfig, Schedule) */
    private long staticDataAgeLimit = defaultAgeLimit5minutes;
    /** Cache age limit for Dynamic data (Predictions, Locations) */
    private long dynamicDataAgeLimit = defaultAgeLimit5minutes;
    
    /**
     * Stacking constructor - allows layering over existing Service Adaptor, or even RMI proxy.
     * @param backing 
     */
    public VOCache(INextbusService backing) {
        underlyingSvc = backing;
    }
    
    public List<Agency> getAgencies() throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Agency getAgency(String id) throws ServiceException {
        // if cacheMissOrTooOld(...) refreshCache(...); 
        // return cache.get(...); 
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PredictionGroup getPredictions(Stop s) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<PredictionGroup> getPredictions(Route route, Collection<Stop> stops) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PredictionGroup getPredictions(Route r, Stop s) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<PredictionGroup> getPredictions(Map<Route, Stop> stops) throws ServiceException {
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
