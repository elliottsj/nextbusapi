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
package net.sf.nextbus.publicxmlfeed.impl.http_rpc;

import net.sf.nextbus.publicxmlfeed.impl.RPCImpl;
import net.sf.nextbus.publicxmlfeed.impl.RPCRequest;
import net.sf.nextbus.publicxmlfeed.service.ServiceException;
import net.sf.nextbus.publicxmlfeed.service.TransientServiceException;
import net.sf.nextbus.publicxmlfeed.service.ServiceConfigurationException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Simplest possible HTTP RPC client with Compression support but NO Connection
 * Pooling or any fancy communications error-retry control. For that you'll have to use Apache
 * Commons HttpClient. 
 *
 * @author jrd
 */
public class JavaNetRPCImpl implements RPCImpl {

    // Declare the logger this way so the IDE Refactor tool hits the Loggers when I move classes.
    private static final Logger logger = Logger.getLogger(JavaNetRPCImpl.class.getName());
    private boolean useGzipCompression;
    private long lastSuccessfulCallTimeUTC;
    private long totalRPCCalls;

    public JavaNetRPCImpl() {
    }

    public void setUseGzipCompression(boolean arg) {
        useGzipCompression = arg;
    }

    public String call(RPCRequest request) throws ServiceException {
        HttpURLConnection c = null;

        BufferedReader rd;
        StringBuilder sb;
        InputStream is;
        String line;
        try {
            URL url = new URL(request.getFullHttpRequest());

            c = (HttpURLConnection) url.openConnection();
            logger.log(Level.FINEST, "RPC handler opened HTTP connection");
            
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            
            /* Ask for Zip compression in the HTTP Header
             * see http://www.nextbus.com/xmlFeedDocs/NextBusXMLFeed.pdf
             */
            if (useGzipCompression) {
                c.setRequestProperty("Accept-Encoding", "gzip, deflate");
                logger.log(Level.FINEST, "RPC handler has set Accept-Encoding to gzip, deflate");
            }
            c.connect();
            int http_status = c.getResponseCode();
            if (http_status != HttpURLConnection.HTTP_OK) {
                String msg = "Received HTTP Status Code "+http_status+" : "+c.getResponseMessage();
                logger.log(Level.WARNING, msg);
                totalRPCCalls++;
                throw new TransientServiceException(msg);
            }
           
            
            /* We have to use a different InputStream type if we are using wire compression */
            if (useGzipCompression) {
                is = new GZIPInputStream(c.getInputStream());
                logger.log(Level.FINEST, "RPC handler has setup a Gzip decompression stream");
            } else {
                logger.log(Level.FINEST, "RPC handler is not using a compression stream.");
                is = c.getInputStream();
            }

            /* Read in the HTTP Response until end of buffer */
            rd = new BufferedReader(new InputStreamReader(is));
            sb = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                sb.append(line);
                logger.log(Level.FINEST, "read "+line.length()+" bytes from the HTTP input buffer");
            }
            
            /* Done! Cleanup and return to the caller */
            is.close();
            rd.close();
            logger.log(Level.FINEST, "RPC handler closed HTTP connection");
            lastSuccessfulCallTimeUTC = System.currentTimeMillis();
            return sb.toString();
        } catch (java.net.MalformedURLException mfu) {
            logger.log(Level.SEVERE, "Invalid URL. Inspect: "+request.getFullHttpRequest(), mfu);
            throw new ServiceConfigurationException(mfu);
        } catch (java.io.IOException ioe) {
            logger.log(Level.WARNING, "During http rpc to nextbus ", ioe);
            throw new TransientServiceException(ioe);
        } finally {
            if (c != null) c.disconnect();
            c = null;
            rd = null;
            sb = null;
            totalRPCCalls++;
        }
    }

    /**
     * Diagnostic method
     * @return the last timestamp (in UTC time) when a Successful RPC was made
     */
    public long getLastSuccessfulCallTimeUTC() {
        return lastSuccessfulCallTimeUTC;
    }

    /**
     * Diagnostic method
     * @return total number of RPC calls (failed and successful) made 
     */
    public long getTotalRPCCalls() {
        return totalRPCCalls;
    }
    
    public void activate() { }

    public void passivate() { }
}
