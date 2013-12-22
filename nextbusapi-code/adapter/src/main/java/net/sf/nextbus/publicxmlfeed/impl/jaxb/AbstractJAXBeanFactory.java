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
package net.sf.nextbus.publicxmlfeed.impl.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.StringWriter;
import org.xml.sax.SAXException;

/**
 * A Bean Factory is responsible for converting XML to/from an Object tree of Java POJOs using JAXB.
 * 
 * Unfortunately, the Object tree built is tightly coupled to the design decisions of the wire protocol
 * designer. That means the wire protocol can change - often quite frequently. In addition, the wire
 * representation of certain data may not convert nicely to Java primitive types.  For that reason
 * wire protocol objects SHOULD NOT BE EXPOSED to a client application - otherwise substantial refactoring
 * ripple effects will occur in the code base every time the service protocol author alters the wire format.
 * 
 * For that reason, XSD mapped Java objects should be translated to Domain Classes by the adapter component.
 * 
 * @author jrd
 */
public abstract class AbstractJAXBeanFactory {

    protected final JAXBContext jaxbContext;
    protected final Schema schema;
    protected final Unmarshaller unmarshaller;

    /**
     * Utility to load XSD files from the classpath. In Unit test mode (IDE), the
     * classpath will be target/classes director. In the J2EE container, the classpath
     * will be the interior of the JAR universe where this code lives. Never rely on
     * direct file I/O inside of a J2EE container ; Java Security Manager policy is
     * in force in this environment.
     *
     * @param resourcePath file resource to load.
     * @return InputStream to load XSD XML.
     */
    private static InputStream loadXMLDocumentFromClasspath(String resourcePath) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
    }

    /**
     *
     * @param packagePath  The package basepath for JAXB XSD schema compiler generated classes.
     * @param xsdResource  Path to the XSD file
     * @throws JAXBException
     * @throws SAXException
     */
    protected AbstractJAXBeanFactory(String packagePath, String xsdResource) throws JAXBException, SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schema = schemaFactory.newSchema(new StreamSource(loadXMLDocumentFromClasspath(xsdResource)));
        jaxbContext = JAXBContext.newInstance(packagePath);
        unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(schema);
    }

    /**
     * Use JAXB to parse XML into Java XSD backed wire protocol objects.
     * 
     * @param arg Raw XML conformant to the XSD of this parser instance.
     * @return JAXB object graph of extracted elements.
     * @throws javax.xml.bind.UnmarshalException 
     */
    public abstract Object parse(String arg) throws JAXBException;

    /**
     * Generic utility to convert a POJO backed by an XSD into its XML serialized stream.
     * This method might not be of any use other than to build a simulator of the native
     * service.
     *
     * @param ref Object to serialize to XML.  Requires that JAXB Object Factories have been
     * initialized properly to marshal this class.
     * @return XML stream
     * @throws RocketBuxProtocolException Wraps the underlying JAXB exception received while trying to serialize an XML bean.
     */
    public final String marshall(Object ref) throws JAXBException {
        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter s = new StringWriter();
        marshaller.marshal(ref, s);
        return s.toString();
    }
}
