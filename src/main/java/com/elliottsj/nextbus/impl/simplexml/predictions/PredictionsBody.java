package com.elliottsj.nextbus.impl.simplexml.predictions;

import com.elliottsj.nextbus.impl.simplexml.Body;
import org.simpleframework.xml.ElementList;

import java.util.List;

public class PredictionsBody extends Body {

    @ElementList(inline = true, required = false)
    private List<Predictions> predictions;

    public List<Predictions> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Predictions> predictions) {
        this.predictions = predictions;
    }

}
