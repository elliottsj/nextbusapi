package com.elliottsj.nextbus.impl.cache;

import com.elliottsj.nextbus.domain.Agency;
import com.elliottsj.nextbus.domain.PredictionGroup;
import com.elliottsj.nextbus.domain.Stop;

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
