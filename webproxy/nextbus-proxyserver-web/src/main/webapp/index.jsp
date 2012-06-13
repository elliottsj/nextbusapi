<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="nbProjectUrl" value="http://sourceforge.net/projects/nextbusapi/"/>
<c:set var="gsonProjectUrl" value="http://code.google.com/p/google-gson/"/>
<c:set var="ctxPath" value="${pageContext.servletContext.contextPath}"/>
<c:set var="nbRawApiUrl" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.servletContext.contextPath}/nextbus-rawapi"/>
<c:set var="jqUrl" value="http://jquery.com/"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>NextBus Java API Web Proxy</title>
        <script src="http://code.jquery.com/jquery-latest.js"></script>
    </head>
    <body style="width: 800px; ">
        Quick JSON Web Proxy Demo for the <a href="${nbProjectUrl}">NextBus Java Adapter Kit.</a><br/>
        <div style="background-color: tan; padding: 5px;"><span >
            <p>
                This self-contained web app serves as a simple demonstration on how to build JSON Support over the Nextbus API.
                The Java EE 6 environment was chosen because of the convenience of packaging and deploying in EAR format, as well
                as the use of Dependency Injection and Annotations to build a layered application. While this Web-app provides a nice
                jumping off point for UI Developers that prefer jQuery and Dojo over JSP or Faces, this is only a starting point, not
                a foundation. The NextBus API is designed around uses-cases for Bulk data transport about a real-time system. A Service
                for Human-centric use cases would need to be built up. The perspective on the data feed would be driven by
                proximity to location and time horizon rather than a bulk state update from the transit network. That said, as a starting
                point, this Web app demonstrates how one might rapidly build a Server-side application module, wrap it with a JSON
                Adapter, and quickly support a large population of mobile clients by deploying the EAR file up in the cloud. The
                JSON Servlet uses a Local Stateless Session Bean injected by the Container and the Excellent Google 
                <a href="gsonProjectUrl" target="_blank">GSON Serializer</a> library. Read the code, there's less than 5 Classes to this
                web app! That's the power of Java EE 6.
            </p></span>
        </div>
        <div id="json">
            <br>
            <center><font size="4"> ${nbRawApiUrl}</font></center><br>
            A rough example is given below as to how to directly invoke using <a href="${jqUrl}" target="_blank">JQuery</a>:<br>
            <pre>
$.ajax({
  type: 'POST',
  url: ${nbRawApiUrl},
  data: { method: 'getRouteConfiguration', callParams: { agency: { id: 'mbta', title: 'Boston MBTA' }, tag: '100', title: 'The 100' }},
  success: success,
  dataType: json
});
            </pre>
            The JSON Servlet expectes two HTTP Post Parameters:
            <ul>
                <li>method:     Nextbus Adapter method to invoke</li>
                <li>callParams: Calling argument, a NextBus Domain class in JSON format.</li>
            </ul>
            A summary of <a href="${ctxPath}/cheatsheet.html">Method Calls and sample JSON arguments</a> that you can call with is here.<br>
            A <a href="${ctxPath}/demoJquery1.jsp">live example</a> using jQuery can be executed (and source viewed) here.<br>
        </div>    
        <div style="background-color: tan;">
            <p>You can also invoke the JSON Servlet using Traditional Form Post. The Call args must be in JSON Format.</p>
            <p>Use the <a href="${ctxPath}/cheatsheet.html">Cheatsheet</a> for some canned examples to try.</p>
            <form method="post" action="${ctxPath}/nextbus-rawapi">
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
        </div>
    </body>
</html>
