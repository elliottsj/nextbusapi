/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.nextbus.html5demo.service;

import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.annotation.Resource;
/**
 *
 * @author jrd
 */
@Startup
@Singleton
public class TestbedEventGenerator {

    private final Logger log = LoggerFactory.getLogger(TestbedEventGenerator.class);

    @EJB
    private EventStreamDemultiplexer demux;
    
    public TestbedEventGenerator() {
        log.info("STARTED!");
    }
    private boolean halted = false;

    @Schedule(second = "*/5", minute = "*", hour = "*")
    private void makeTraffic() {
        while (true) {
            Map value = new HashMap();
            value.put("time", System.currentTimeMillis());
            value.put("type", "testcase");
            value.put("val", 1);
            demux.distributeEvents(value);
            log.info("timed task added test event.");
        }
    }

    public void stop() {
        halted = true;
    }

}
