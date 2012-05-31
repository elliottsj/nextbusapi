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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility for our Junit tests - Load XML files from the test classpath.
 * 
 * @author jrd
 */
public class TestUtil {
    /**
     * Utility to load Sample XML's off the Test run classpath.
     * @param resourcePath filename to load
     * @return contents of the file
     * @throws Exception 
     */
    public static String loadXMLDocumentFromClasspath(String resourcePath) {
        // reads the file off the Test run classpath
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        try {
        String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            is.close();
        } catch (java.io.IOException io) {
            throw new RuntimeException("Some problem retrieving a file from the classpath...");
        }
        return sb.toString();
    }
    
}
