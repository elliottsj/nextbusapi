<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>NextBus Java API Web Proxy</title>
    </head>
    <body style="width: 800px;">
        This is the Companion Demo Web-app for the <a href="http://sourceforge.net/projects/nextbusapi/">NextBus Java Adapter Kit.</a><br/>
        <div >
            <p>

                This was designed to illustrate how to use the Adapter JAR in a Java EE 6 environment with JSON Support for UI Developers.
                While the Domain POJOs in the original adapter project suffice for JSP/JSTL/JFC and Faces developers, JSON is prefered
                my many developers who prefer using Dojo or JQuery over dealing with Java Servlet and the IDE Environment.
            </p>
            <p>
                This EJB EAR project is also an excellent jumping off point for developers that want to write serious Mobile thin-client applications either for the Phone or the Tablet.
                While the Raw JSON API is really not well-suited for thin-client developers, the project illustrates the concepts of packaging,
                and injection of business-tier classes in Servlets which arranges data in a representation for a remote client.
            </p>
            <p>

            </p>
        </div>
        <div>
            <p>Servlet net.sf.nextbus.servlet.GetAgenciesServlet is mapped to /getAgencies<br>
                It will generate a JSON response containing all of the Agency instances known to the NextBus adapter.<br>
                You can <a href="${pageContext.servletContext.contextPath}/getAgencies" target="_blank">click here</a> to see the results.
            </p>
        </div>

        <div>
            <p>Form Post Builder for JSON RPC to the NextbusRawJSONServlet mapped on /nextbus-rawapi</p>
            <form method="post" action="${pageContext.servletContext.contextPath}/nextbus-rawapi">
                <select name="method">
                    <option>getAgency</option>
                    <option>getRoutes</option>
                    <option>getRouteConfiguration</option>
                    <option>getVehicleLocations</option>
                    <option>getPredictions</option>
                    <option>getSchedule</option>

                </select>
                <input type="text" name="callParams" size="40" value="{ }"/>
                <input type="submit"/>
            </form>
            <table style="border: 2px; ">
                <thead><th>method</th><th>callparams</th><th>Returns...</th></thead>
                <tr><td>getAgency</td><td>{ agency: &lt;arg&gt; }</td><td>A JSON Agency object</td></tr>
                <tr><td>getRoutes</td><td>A JSON Agency Object</td><td>A List of JSON Route objects</td></tr>
                <tr><td>getRouteConfiguration</td><td>A JSON Route Object</td><td>A JSON RouteConfiguration composite object</td></tr>
                <tr><td>getVehicleLocations</td><td>A JSON Route Object</td><td>A List of JSON Vehicle Location Objects</td></tr>
                <tr><td>getPredictions</td><td>A List of JSON Stop Objects</td><td>A JSON PredictionGroup Object</td></tr>
                <tr><td>getSchedule</td><td>A JSON Route Object</td><td>A JSON Schedule Object</td></tr>
            </table>
                <br>
                <a href="${pageContext.servletContext.contextPath}/cheatsheet.html" target="_blank">Cheat Sheet!</a>
        </div>

    </body>
</html>
