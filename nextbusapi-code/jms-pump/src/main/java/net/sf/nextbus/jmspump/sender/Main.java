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

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bootstrap for a String Integration based standalone daemon program. This has
 * to support Standalone/IDE mode as well as JSVC daemon mode, hence the
 * complexity with some static attributes, and various callback methods.
 */
public class Main implements Daemon {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static ClassPathXmlApplicationContext springCtx;
    private static Task task;
    private static boolean jsvcDaemonMode = false;
    private static String greeting=
    "**********************************************************\n"+
    "****** NextBus Pump Adapter for JMS/ActiveMQ\n"+
    "****** $Id$ \n"+
    "****** http://sourceforge.net/projects/nextbusapi/ \n"+
    "**********************************************************";
    
    /**
     * Universal init(), for both jsvc and standalone mode.
     */
    public static void init() {
        if (springCtx != null) {
            return;  // at most once!
        }        // Bootstrap Spring Framework...
        springCtx = new ClassPathXmlApplicationContext(new String[]{
                    "nbpump-spring-context.xml",
                    "activemq-jms-config.xml"
                });

        // Get a reference task object, so we can turn it on in the start() method.
        task = springCtx.getBean(Task.class);
        
        // Create a shutdown handler to gracefully halt in-flight work...
        springCtx.addApplicationListener(new TaskSchedulerShutdownHandler());
    }

    /**
     * main() is never invoked by Commons Daemon (jsvc), but we do need an
     * implementation that supports command line operation and IDE testing
     * outside of jsvc.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println(greeting);
        
        // Register a Shutdown Hook to deal with CTRL-C or SIGTERM, on shutdown
        // we'll close Spring. Spring can then let it's own event listeners know.
        // In JSVC daemon mode, the destroy() callback does the same thing 
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (springCtx != null) {
                    springCtx.close();
                }
                log.info("*** Started Spring shutdown...");
            }
        });
        
        // cmd line args ; search for only one, -daemon, for jsvc mode.
        if (args != null) {
            for (String arg : args) {
                if (arg.equals("-daemon")) {
                    jsvcDaemonMode = true;
                }
            }
            if (jsvcDaemonMode) {
                System.out.println("      -- jsvc daemon mode");
            }
        }
        
   
        init();
        // Finally, invoke the start method
        if (!jsvcDaemonMode) {
            new Main().start();
        }
        log.info("started... main thread waiting for termination (signal or CTRL-C) ");
    }

    /**
     * Shutdown hook to passivate the Spring scheduler and executor. In a real
     * JMS implementation, you'd want to wait for any inflight message traffic
     * trapped inside of Spring Integ's internal queues to be flushed to their
     * output channels before terminating the daemon.
     */
    static class TaskSchedulerShutdownHandler implements ApplicationListener<ContextClosedEvent> {
        public void onApplicationEvent(ContextClosedEvent event) {
            ThreadPoolTaskExecutor executor = springCtx.getBean(ThreadPoolTaskExecutor.class);
            ThreadPoolTaskScheduler scheduler = springCtx.getBean(ThreadPoolTaskScheduler.class);
            if (executor != null) {
                executor.shutdown();
            }
            if (scheduler != null) {
                scheduler.shutdown();
            }
            log.info("*** Shutdown Spring scheduler and executor...");
        }
    }

    /**
     * Apache Commons Daemon (jsvc) lifecycle callback.
     */
    public void destroy() {
        springCtx.close();
    }

    /**
     * Apache Commons Daemon (jsvc) lifecycle callback.
     */
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        init();
    }

    /**
     * Apache Commons Daemon (jsvc) lifecycle callback.
     */
    public void start() throws Exception {
        task.setPaused(false);
    }

    /**
     * Apache Commons Daemon (jsvc) lifecycle callback.
     */
    public void stop() throws Exception {
        task.setPaused(true);
    }
}
