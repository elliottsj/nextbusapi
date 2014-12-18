/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elliottsj.nextbus.domain;

/**
 * By creating an Interface for all Geocoded types, it's very easy to implement generic proximity sorts.
 * At first this Interface looks like excess from Overanalysis or excessive OOAD, but once you dive into
 * implementing domain classes, you identify patterns that motivate you to further segregate types. 
 * This interface allows me to sort Stops and Vehicle locations - which really is the whole point of
 * having geocodes in the first place. 
 * 
 * @author jrd
 */
public interface IGeocoded {
    /**
     * Returns the GPS location of a Geocodeable object.
     * @return 
     */
    public Geolocation getGeolocation();   
}
