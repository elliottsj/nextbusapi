<%@ page contentType="text/html" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="css" value="/css/html5demo.css" />
<!DOCTYPE html>
<html>
    <head>
        <title>Info</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="cache-control" content="max-age=0" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta http-equiv="expires" content="0" />
        <meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
        <meta http-equiv="pragma" content="no-cache" />
        <link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css" />
        <link rel="stylesheet" href="${css}" type="text/css"/>
        <style>
            label, input {
                text-align: center;
            }
        </style>
        <script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
        <script src="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js"></script>
    </head>
    <body>
        <div data-role="page">
            <header data-role="header">
                <div data-role="navbar">
                    <ul>
                        <c:url var="locationUrl" value="/navigator" ><c:param name="to" value="location"/></c:url>
                        <li><a href="${locationUrl}" data-ajax="false" data-icon="home" >Location</a></li>
                        <c:url var="eventsUrl" value="/navigator"><c:param name="to" value="events"/></c:url>
                        <li><a href="${eventsUrl}" data-icon="home">Events</a></li>
                        <li><a class="ui-btn-active" data-icon="home">Info</a></li>
                    </ul>
                </div>
                <div id="warnings"></div>
            </header>
            <section data-role="section">
                <h2>Architectural Proof of Concept</h2>
                <h3>HTML 5 Geolocation & Server Side Events API driven with Java EE 6 / ActiveMQ<h3>
                <br>
                &copy;2013 James R. Doyle  [mailto:rockymtnmagic@gmail.com]<br>
                <a href="https://sourceforge.net/projects/nextbusapi/">Project Overview</a><br>
                <a href="https://sourceforge.net/p/nextbusapi/code/HEAD/tree/trunk/html5demo/">SVN Source Repository</a><br>
            </section>
            <footer data-role="footer">
            </footer>
        </div>
    </body>
</html>