/**
 * *****************************************************************************
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
 * Usage of the NextBus Web Service and its data is subject to separate Terms
 * and Conditions of Use (License) available at:
 *
 * http://www.nextbus.com/xmlFeedDocs/NextBusXMLFeed.pdf
 *
 *
 * NextBus® is a registered trademark of Webtech Wireless Inc.
 *
 *****************************************************************************
 */
package net.sf.nextbus.jmspump.sender;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bootstrap for a String Integration based standalone daemon program. In this
 * case Spring wires up POJOs from the top down using whatever we tell it in
 * applicationContext.xml
 */
public class Main {

    static final Logger log = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) throws Exception {
        org.apache.log4j.BasicConfigurator.configure();
        
        // Bootstrap Spring
        ClassPathXmlApplicationContext springCtx;
        springCtx = new ClassPathXmlApplicationContext(new String[]{
                    "applicationContext.xml",
                    "activemq-jms-config.xml"
                });
        springCtx.addApplicationListener(new TaskSchedulerShutdownHandler());
        log.info("started... main thread waiting for termination (signal or CTRL-C) ");
        // When Spring's scheduler thread exits, this task will terminate as well
    }

    /**
     * Shutdown hook to passivate the Spring scheduler and executor. In a real JMS implementation,
     * you'd want to wait for any inflight message traffic trapped inside of Spring Integ's
     * internal queues to be flushed to their output channels before terminating the daemon.
     */
    static class TaskSchedulerShutdownHandler implements ApplicationListener<ContextClosedEvent> {

        ThreadPoolTaskExecutor executor;
        ThreadPoolTaskScheduler scheduler;

        public void onApplicationEvent(ContextClosedEvent event) {
            scheduler.shutdown();
            executor.shutdown();
            log.info("passivated Spring scheduler.");
        }
    }
}
