package net.sf.nextbus.publicxmlfeed.impl.simplexml.predictions;

import java.util.List;

public class Body extends net.sf.nextbus.publicxmlfeed.impl.simplexml.Body {

    private List<Predictions> predictions;

    public List<Predictions> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Predictions> predictions) {
        this.predictions = predictions;
    }

}
