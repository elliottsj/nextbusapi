/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.nextbus;
import net.sf.nextbus.publicxmlfeed.domain.*;
import flexjson.JSONSerializer;
import flexjson.JSONDeserializer;
import org.junit.Assert;
import org.junit.Test;
/**
 *
 * @author jrd
 */
public class TestJSONHypothesis {
    
    static JSONSerializer java2json = new JSONSerializer();
    static JSONDeserializer json2java = new JSONDeserializer();

    @Test
    public void testJson1() {
        Agency a = new Agency("test","test","test","test","test");
        
        String jsonOut  = java2json.deepSerialize(a);
        System.out.println(jsonOut);
       
        
    }
    
    @Test
    public void testJsonIn() {
        Agency a = (Agency) json2java.deserialize("{id:test, title: test }", Agency.class);
        System.out.println(a);
        Route r = (Route) json2java.deserialize("{agency: {id: mbta, title: Boston MBTA }, tag: 101B, title: Express To Boston }", Route.class);
        Assert.assertNotNull(r.getAgency());
        System.out.println(r);
        
        Stop s = (Stop) json2java.deserialize("{tag: s3413, title: Rt 28S and Warren, route: { agency: {id: mbta, title: Boston MBTA }, tag: 101B, title: Express To Boston } }", Stop.class);
        Assert.assertNotNull(s.getRoute());
        Assert.assertNotNull(s.getRoute().getAgency());
        System.out.println(s);
    }
    
}
