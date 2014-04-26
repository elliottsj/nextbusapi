package net.sf.nextbus.publicxmlfeed.impl.cache;

import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.PredictionGroup;
import net.sf.nextbus.publicxmlfeed.domain.Stop;

import java.util.List;

/**
 *
 */
public interface ICacheStore {

    public boolean isAgenciesCached();
    public long getAgenciesAge();
    public List<Agency> getAgencies();
    public void putAgencies(List<Agency> agencies);

    public boolean isPredictionsCached(Stop stop);
    public long getPredictionsAge(Stop stop);
    public List<PredictionGroup> getPredictions(Stop stop);
    public void putPredictions(Stop stop, List<PredictionGroup> predictionGroups);

}
