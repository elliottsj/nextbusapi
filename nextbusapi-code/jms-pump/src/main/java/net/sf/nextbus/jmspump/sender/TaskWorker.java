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
import java.util.Collection;

/**
 * A base class for the Task Workers.
 * @author jrd
 */
public abstract class TaskWorker {
        boolean enabled = true;
        /** timestamp of last successful work action */
        long lastTime;
        /** age limit */
        long limit;
        /** count of errors during execution phase */
        int errors;
        /** count of successes during execution phase */
        int success;

        public boolean isOld() {
            return System.currentTimeMillis() - lastTime >= limit;
        }
        
        /**
         * NOTE: This method should not propagate an Exceptions...
         * 
         * @return
         */
        public abstract Collection execute();
        
        /**
         * 
         * @return 
         */
        protected Collection empty() { return new java.util.HashSet(); }
}
