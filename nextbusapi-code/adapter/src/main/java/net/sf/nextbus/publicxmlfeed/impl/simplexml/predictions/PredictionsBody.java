package net.sf.nextbus.publicxmlfeed.impl.simplexml.predictions;

import org.simpleframework.xml.ElementList;

import java.util.List;

public class PredictionsBody extends net.sf.nextbus.publicxmlfeed.impl.simplexml.Body {

    @ElementList(inline = true)
    private List<Predictions> predictions;

    public List<Predictions> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Predictions> predictions) {
        this.predictions = predictions;
    }

}
