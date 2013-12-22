package net.sf.nextbus;

import net.sf.nextbus.publicxmlfeed.domain.*;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author jrd
 */
public class TestJSONHypothesis {

    private Gson gson;
    @Before
    public void setup() {
        gson = new Gson();
    }
    
 @Test
    public void testJsonIn() {
        Agency a = gson.fromJson("{id:'test', title: 'test' }", Agency.class);
        System.out.println(a);
    
        Route r = gson.fromJson("{agency: {id: 'mbta', title: 'Boston MBTA' }, tag: '100', title: 'Express To Boston' }", Route.class);
        Assert.assertNotNull(r.getAgency());
        System.out.println(r);

        Stop s = (Stop) gson.fromJson("{tag: '5270', title: 'Wellington Station Busway', route: { agency: {id: 'mbta', title: 'Boston MBTA' }, tag: '100', title: 'Route 100 Malden/Medford/Everett' } }", Stop.class);
        Assert.assertNotNull(s.getRoute());
        Assert.assertNotNull(s.getRoute().getAgency());
        System.out.println(s);
    }

    @Test
    public void testFoo() {
        
        
       
        
        String s = "{ agency: { id: 'mbta', title: 'Boston MBTA' }, tag: '100', title: 'The 100' }";
        Route r = gson.fromJson(s, Route.class);
        System.out.println(r);
    }
}
