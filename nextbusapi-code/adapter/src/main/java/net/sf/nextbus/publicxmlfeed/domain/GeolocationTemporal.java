package net.sf.nextbus.publicxmlfeed.domain;

/**
 *
 */
public class GeolocationTemporal extends Geolocation {

    /**
     * ctor.
     *
     * @param lat Degrees latitude. Negative values are South, Positive values
     *            are North.
     * @param lon Degrees longitude. Negative values are West, Positive values
     */
    public GeolocationTemporal(double lat, double lon) {
        super(lat, lon);
    }

}
