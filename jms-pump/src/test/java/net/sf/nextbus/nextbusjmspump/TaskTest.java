package net.sf.nextbus.nextbusjmspump;

import junit.framework.Assert;
import net.sf.nextbus.jmspump.sender.Task;
import org.junit.Before;
import org.junit.Test;
import net.sf.nextbus.publicxmlfeed.service.INextbusService;
import net.sf.nextbus.publicxmlfeed.impl.SimplestNextbusServiceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Unit test for simple App.
 */
public class TaskTest {
    
    final Logger log = LoggerFactory.getLogger(TaskTest.class);
    
    private INextbusService svc ;
    @Before
    public void setup() {
        svc = new SimplestNextbusServiceAdapter();
    }
    
    @Test
    public void TestTaskExecute() throws Exception {
        Task t = new Task(svc, "mbta");
        t.setCacheExpiration(1000*60);
        for (int i=0; i<300; i++) {
         t.execute();
         Thread.sleep(1000);
        }
        Assert.assertTrue(t.getSuccessfulRuns()>1);
    }
}
