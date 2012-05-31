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

import net.sf.nextbus.publicxmlfeed.service.IServiceRemote;
import net.sf.nextbus.publicxmlfeed.service.IService;
import net.sf.nextbus.publicxmlfeed.service.TransientServiceException;
import java.rmi.registry.*;

/**
 * The Simplest Possible RMI Client. This does the Name Service lookup, binds
 * the remote object to a Proxy - and returns a Service. The RMI details are
 * utterly transparent to the API developer who needs to use the service.
 *
 * @author jrd
 */
public class RMIClient {

    private String hostName;
    private int port = 1099;  // standard RMID port
    private String serviceName = net.sf.nextbus.publicxmlfeed.impl.rmiproxy.RMIServerStub.class.getName();  // Default service name in RMI

    /**
     * Ctor with full override of communications parameters
     *
     * @param _hostName
     * @param _port
     * @param _serviceName
     */
    public RMIClient(String _hostName, int _port, String _serviceName) {
        this.hostName = _hostName;
        this.port = _port;
        this.serviceName = _serviceName;
    }

    /**
     * Simple case ctor. Defaults to Port 1099 and the default Service stub
     * name.
     *
     * @param _hostName Server name of IP Address String where the RMI Adapter
     * runs.
     */
    public RMIClient(String _hostName) {
        this.hostName = _hostName;
    }

    /**
     * Gets a reference to a NextBus Webservice adapter via RMI on another host.
     *
     * @return reference to the Adapter Service interface
     * @exception TransientServiceException wraps all underlying checked RMI
     * exception
     */
    public IService getService() {
        try {

            Registry registry = LocateRegistry.getRegistry(hostName, port);
            IServiceRemote stub = (IServiceRemote) registry.lookup(serviceName);
            IService svc = new RMIClientProxy(stub);
            return svc;
        } catch (java.rmi.ConnectException cn) {
            throw new TransientServiceException("This happens when the RMID is not running", cn);
        } catch (java.rmi.RemoteException foo) {
            throw new TransientServiceException("Something bad happened during RMI", foo);
        } catch (java.rmi.NotBoundException nbe) {
            throw new TransientServiceException("Aint no findin' no serviceName " + serviceName + " at no host named " + hostName + " an' port " + port);
        }

    }
}