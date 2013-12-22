	NextBus Adapter For Java - Built Against Specification V1.18

This Adapter project provides a clean Java API adapter to the NextBus
Public XML datafeed. The design goals were to provide a comprehensive
implementation that included Unit Tests to achieve long-term maintainability
and universality for the community of Java developers. The code honors
the goals of loose-coupling and encapsulation. As a result, it is narrow
enough to serve the goals of both the Mobile Device developer, as well as the
Java Enterprise developer who will inevitably be focusing more on back-end issues.

For the curious and impatient, I've included a downloadable executable
JAR that will launch the BeanShell console. This gives you 
instant gratification access to touch the API methods and try things out 
without having to go into your IDE and setup a project.  

     1.  Download the uber-jar from the releases/ download directory.
     2.  Run as follows:
             $ java -jar nextbus-beanshelled.jar
         
	 Beanshell will start with a GUI and shell window that you can
         feed with commands from the next step.
     3.  Use the sample script in releases/Nextbus-Beanshell-Howto.txt

     

While you can dive right in, the Javadoc should also be downloaded 
from the releases/ file area. Unzip it and point the browser at 'index.html'

Please take time to browse the UML class diagrams that are included
in the Javadocs. These help to visually understand the domain classes
that were distilled from analysis of the NextBus XML API spec. Keep
in mind that some of the classes are Nested due to their inherent 
aggregation relationship.  You'll also learn from the Javadoc how
to instantiate the Service Adapter, how to call, and what Exceptions
to expect. There's also an RMI Proxy Server and additional clues for
customizing the HTTP socket factory.

In particular, take note of the Classes for RouteConfiguration,
Schedule and Geolocation as these were designed with additional
finder and sort methods to help achieve the most common uses cases.

-- Jim Doyle     3 Jun 2012

