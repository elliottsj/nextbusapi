package net.sf.nextbus.publicxmlfeed.domain;

import java.util.Date;

/**
 *
 */
public class GeolocationTemporal extends Geolocation implements TemporalValueObject {

    /** Creation time in milliseconds since epoch */
    private long createTime;

    /**
     * ctor.
     *
     * @param lat Degrees latitude. Negative values are South, Positive values
     *            are North.
     * @param lon Degrees longitude. Negative values are West, Positive values
     */
    public GeolocationTemporal(double lat, double lon, int secsSinceReport) {
        super(lat, lon);
        createTime = System.currentTimeMillis() - 1000 * secsSinceReport;
    }

    /**
     * Gets the creation timestamp of the object in milliSeconds since 1 January 1970 00:00:00 UTC
     *
     * @return birth date from the Unix epoch.
     */
    public long getObjectTimestamp() {
        return createTime;
    }

    /**
     * Gets the creation timestamp of the object
     *
     * @return the creation timestamp of the object
     */
    public Date getTimestamp() {
        return new Date(createTime);
    }

    /**
     * Gets the current age of the object in Seconds.
     *
     * @return current age of the object in Seconds.
     */
    public long getObjectAge() {
        return (System.currentTimeMillis() - createTime) / 1000;
    }

    /**
     * Tests the age of object since its creation time.
     *
     * @param seconds a number of seconds
     * @return true iff the object is currently older than 'seconds' given
     */
    public boolean isObjectOlderThan(long seconds) {
        return getObjectAge() > seconds;
    }
}
