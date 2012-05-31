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
package net.sf.nextbus.publicxmlfeed.impl.rmiproxy;
import java.util.logging.Logger;

/**
 * Simplest RMI Server Main - Manages Nameserver Binding, Server Shutdown and Unbind.
 * <p>
 * This is not intended for real-world deployment, rather, it demonstrates the powerful
 * principle of Interface abstraction combined with Remoting.
 * </p>
 * 
 * How to Start and Run the RMI Adapter <p> Start the RMID, Then Start RMI
 * Server. Note, the JAR file contains all the classes you need. It's just a
 * matter of setting Classpath and RMI Codebase to the JAR.
 *
 * <pre>
 * $
 * $ $JDK_HOME/bin/rmid -port 1099 -log /var/run/rmid/log &
 * $
 * $ java -cp adapter-1.0-SNAPSHOT.jar -Djava.rmi.server.hostname=192.168.11.2 \
 * -Djava.rmi.server.codebase=file:/abyss/home/jrd/adapter-1.0-SNAPSHOT.jar \
 *  net.sf.nextbus.publicxmlfeed.impl.rmiserver.RMIServerMain
 * May 24, 2012 5:37:54 PM net.sf.nextbus.publicxmlfeed.impl.rmiproxy.RMIServerMain main
 * INFO: Shutdown handler is registered
 * May 24, 2012 5:37:55 PM net.sf.nextbus.publicxmlfeed.impl.rmiproxy.RMIServerStub <init>
 * INFO: Server is started and listening for connections.... Press CTRL-C or send SIGTERM to stop this server instance.
 * 
 * ...
 * </pre>
 * In this example, your RMI client should point to 192.168.11.2 Port 1099,
 * as this is the location of this machine's nameserver.
 * <p>
 * Hit CTRL-C when done with with and the server will clean up its binding in the
 * RMI Nameserver.
 *</p>
 * @author jrd
 */
public class RMIServerMain {

    protected static final String bindingName = RMIServerStub.class.getName();
    protected static RMIServerStub servant;
    protected static Logger log;
    private static boolean hasBinding;

    public static void main(String[] args) throws Exception {
        log = Logger.getLogger(bindingName);
        Runtime.getRuntime().addShutdownHook(new ShutdownHandler());

        log.info("Shutdown handler is registered");
        start();
        
        // This will force the Main thread to permanently go to sleep.... 
        // The Unix signal handlers will awaken the JVM and cause the Shutdown handler
        // to fire as the JVM tears itself down as a Unix process...
        Object p = new Object();
        synchronized (p) {
            p.wait();
        }
    }

    /**
     * Starts up the RMI Server
     *
     * @throws Exception
     */
    public static void start() throws Exception {
        try {
            servant = new RMIServerStub();
            java.rmi.Remote check = java.rmi.Naming.lookup(bindingName);
            log.warning("An object with the name"+bindingName+" is already registered in the RMI Naming Service. Replacing registration.");
            java.rmi.Naming.rebind(bindingName, servant);
            hasBinding = true;
        } catch (java.rmi.NotBoundException this_is_good) {
            java.rmi.Naming.bind(bindingName, servant);
            log.info("Server is started and listening for connections.... Press CTRL-C or send SIGTERM to stop this server instance.");
            hasBinding = true;
        }
        if (hasBinding == false) {
            throw new RuntimeException("Cannot Start Server - another instance is already bound in the rmid.");
        }
         log.info("Bound servant handle to "+bindingName);
    }

    /**
     * On process exit/shutdown - this unbinds the object from the local RMI
     * Binding Registry
     */
    public static class ShutdownHandler extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                log.info("About to remove binding handle from registry.");
                if (hasBinding) {
                    java.rmi.Naming.unbind(bindingName);
                }
                System.out.println("Server has stopped listening at the RMI Server URL");

            } catch (java.rmi.NotBoundException nbe) {
                // swallow - unexpected
            } catch (java.rmi.RemoteException re) {
                // swallow - only happens if RMIregistry is killed while server is running
            } catch (java.net.MalformedURLException ue) {
                // swallow - unexpected.
            }
        }
    }
}
