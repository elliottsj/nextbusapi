/*******************************************************************************
 * Copyright (C) 2011,2012 by James R. Doyle
 *
 * This file is part of the NextBus® Livefeed Java Adapter (nblf4j). See the
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership and licensing.
 *
 * nblf4j is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 *
 * nblf4j is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UJMP; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 *
 * Usage of the NextBus Web Service and its data is subject to separate
 * Terms and Conditions of Use (License) available at:
 * 
 *      http://www.nextbus.com/xmlFeedDocs/NextBusXMLFeed.pdf
 * 
 * 
 * NextBus® is a registered trademark of Webtech Wireless Inc.
 *
 ******************************************************************************/
package net.sf.nextbus.publicxmlfeed;

import net.sf.nextbus.publicxmlfeed.impl.jaxb.AbstractJAXBeanFactory;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the correctness of the reverse-engineered XSD schema by feeding XML
 * samples taken from NextBus and feeding them into our Bean Factory. These
 * tests, along with the XSD's need long term care and feeding as the vendor's
 * Web protocol surely will change.
 *
 * @author jrd
 */
public class BeanFactoryTest {

    /**
     * A Test Harness to drive the JAXB Bean Factories.
     *
     * @param <T> Type of Bean Factory Class to instantiate and drive.
     * @param filepath Test XML file to feed to the bean factory.
     * @param parserType Class of Bean Factory Class to instantiate and drive.
     * @throws Exception More than likely a JAXB Exception from parsing something different than expected
     */
    private <T extends AbstractJAXBeanFactory> void testXSD(String filepath, Class<T> parserType) throws Exception {

        AbstractJAXBeanFactory parser = parserType.newInstance();
        Object p = parser.parse(TestUtil.loadXMLDocumentFromClasspath(filepath));
        Assert.assertNotNull(p);
        if (p instanceof java.util.Collection) {
            Collection c = (Collection) p;
            Assert.assertTrue(c.size() > 0);
        }
    }

    /**
     * **************************************************************************
     * Positive Response Cases - using sample XML saved from the server endpoint
     ***************************************************************************
     */
    @Test
    public void agencyListResponsePositive() throws Exception {
        testXSD("positive-response-cases/agencylist/testcase.xml", net.sf.nextbus.publicxmlfeed.impl.jaxb.AgencyListServiceBeanFactory.class);
    }

    @Test
    public void routeConfigResponsePositive() throws Exception {
        testXSD("positive-response-cases/routeconfig/testcase.xml", net.sf.nextbus.publicxmlfeed.impl.jaxb.RouteConfigServiceBeanFactory.class);
    }

    @Test
    public void routeListResponsePositive() throws Exception {
        testXSD("positive-response-cases/routelist/testcase.xml", net.sf.nextbus.publicxmlfeed.impl.jaxb.RouteListServiceBeanFactory.class);
    }

    @Test
    public void vehicleLocationReponsePositive() throws Exception {
        testXSD("positive-response-cases/vehiclelocation/testcase.xml", net.sf.nextbus.publicxmlfeed.impl.jaxb.VehicleLocationServiceBeanFactory.class);
    }

    @Test
    public void predictionResponsePositive() throws Exception {
        testXSD("positive-response-cases/prediction/testcase.xml", net.sf.nextbus.publicxmlfeed.impl.jaxb.PredictionServiceBeanFactory.class);
    }

    @Test
    public void multiPredictionResponsePositive() throws Exception {
        testXSD("positive-response-cases/prediction-multistop/testcase.xml", net.sf.nextbus.publicxmlfeed.impl.jaxb.PredictionServiceBeanFactory.class);
    }
    
    @Test
    public void scheduleResponsePositive() throws Exception {
        testXSD("positive-response-cases/schedule/testcase.xml", net.sf.nextbus.publicxmlfeed.impl.jaxb.ScheduleServiceBeanFactory.class);
    }
    
    /* TODO - Implement Support for Nextbus Message RPC once the attributes are better understood.
    @Test
    public void messagesResponsePositive() throws Exception {
        testXSD("positive-response-cases/messages/testcase.xml", net.sf.nextbus.publicxmlfeed.impl.jaxb.ServiceMessagesServiceBeanFactory.class);
    }
    * 
    */
    
    /**
     * ******************** <Error> node tests ******************************
     * Send the <Error> response. The factories should be able to parse this
     * response with no error
     ***********************************************************************
     */
    String s = "positive-response-cases/error-response.xml";

    @Test
    public void testErrorResponseRouteCfg() throws Exception {
        testXSD(s, net.sf.nextbus.publicxmlfeed.impl.jaxb.RouteConfigServiceBeanFactory.class);
    }

    @Test
    public void testErrorResponseRouteList() throws Exception {
        testXSD(s, net.sf.nextbus.publicxmlfeed.impl.jaxb.RouteListServiceBeanFactory.class);
    }

    @Test
    public void testErrorResponsePredictionSvc() throws Exception {
        testXSD(s, net.sf.nextbus.publicxmlfeed.impl.jaxb.PredictionServiceBeanFactory.class);
    }

    @Test
    public void testErrorResponseVhcLocn() throws Exception {
        testXSD(s, net.sf.nextbus.publicxmlfeed.impl.jaxb.VehicleLocationServiceBeanFactory.class);
    }
    @Test
    public void testErrorResponseSchedules() throws Exception {
        testXSD(s, net.sf.nextbus.publicxmlfeed.impl.jaxb.ScheduleServiceBeanFactory.class);
    }
    /**
     * **************** Garbage XML Test Cases ********************************
     * Send a well-formed XML totally unrelated to the documented wire protocol
     * and verify that the expected parser exception is dropped.
     **************************************************************************
     */
    private String negative = "negative-response-cases/invalid-garbage.xml";

    @Test(expected = javax.xml.bind.UnmarshalException.class)
    public void routeConfigResponseGarbage() throws Exception {
        testXSD(negative, net.sf.nextbus.publicxmlfeed.impl.jaxb.RouteConfigServiceBeanFactory.class);
    }

    @Test(expected = javax.xml.bind.UnmarshalException.class)
    public void routeListResponseGarbage() throws Exception {
        testXSD(negative, net.sf.nextbus.publicxmlfeed.impl.jaxb.RouteListServiceBeanFactory.class);
    }

    @Test(expected = javax.xml.bind.UnmarshalException.class)
    public void vehicleLocationReponseGarbage() throws Exception {
        testXSD(negative, net.sf.nextbus.publicxmlfeed.impl.jaxb.VehicleLocationServiceBeanFactory.class);
    }

    @Test(expected = javax.xml.bind.UnmarshalException.class)
    public void predictionResponseGarbage() throws Exception {
        testXSD(negative, net.sf.nextbus.publicxmlfeed.impl.jaxb.PredictionServiceBeanFactory.class);
    }

    @Test(expected = javax.xml.bind.UnmarshalException.class)
    public void scheduleResponseGarbage() throws Exception {
        testXSD(negative, net.sf.nextbus.publicxmlfeed.impl.jaxb.ScheduleServiceBeanFactory.class);
    }

    /* TODO - Implement Support for Nextbus Message RPC once the attributes are better understood.
     * 
    @Test(expected = javax.xml.bind.UnmarshalException.class)
    public void messagesResponseGarbage() throws Exception {
        testXSD(negative, net.sf.nextbus.publicxmlfeed.impl.jaxb.ServiceMessagesServiceBeanFactory.class);
    }
    */
}
