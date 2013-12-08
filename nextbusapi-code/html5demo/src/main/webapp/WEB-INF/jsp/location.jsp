<%@ page contentType="text/html" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="css" value="/css/html5demo.css" />
<!DOCTYPE html>
<html>
    <head>
        <title>Geolocation+SSE Demo</title>
        <meta charset="utf-8">
        <meta http-equiv="cache-control" content="max-age=0" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta http-equiv="expires" content="0" />
        <meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
        <meta http-equiv="pragma" content="no-cache" />
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css" />
        <c:url var="sitecss" value="/css/html5demo.css"/>
        <link rel="stylesheet" href="${sitecss}" type="text/css"/>
        <script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
        <script src="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js"></script>
        <c:url var="sitejs" value="/js/html5demo.js"/>
        <script src="${sitejs}"></script>
    </head>
    <body>
        <div id="location-page" data-role="page">
            <header data-role="header">
                <div data-role="navbar">
                    <ul>
                        <li><a href="#" data-icon="home" class="ui-btn-active">Location</a></li>
                            <c:url var="eventsUrl" value="/navigator"><c:param name="to" value="events"/></c:url>
                        <li><a href="${eventsUrl}" >Events</a></li>
                            <c:url var="infoUrl" value="/navigator" ><c:param name="to" value="info"/></c:url>
                        <li><a href="${infoUrl}" data-icon="home">Info</a></li>
                    </ul>
                </div>
                <div id="warnings">None.</div>
            </header>
            <section data-role="section">
                <div  id="location-control" class="ui-grid-a">
                    <button id="getPositionButton">Update Current Location</button>
                    <c:url var="setLocationActionUrl" value="/location"/>
                    <form class="origin" action="${setLocationActionUrl}" method="post">
                        <!-- form prepopulation from session -->
                        <c:set var="latitude" value="${requestScope.latitude}"/>
                        <c:set var="longitude" value="${requestScope.longitude}"/>
                        <c:set var="range" value="${requestScope.range}"/>
                        <c:set var="rangeUnits" value="${requestScope.units}"/>
                        <fieldset class="ui-grid-a">
                            <div class="ui-block-a">
                                <input type="number" data-mini="true" id="latitude" name="latitude" required min="-90.0" max="90.0" value="${latitude}"/>
                                <label for="latitude">latitude (&deg;N)</label>
                            </div><div class="ui-block-b">
                                <input type="number" data-mini="true" id="longitude" name="longitude" required min="-180.0" max="180.0" value="${longitude}" />
                                <label for="longitude">longitude (&deg;W)</label>
                            </div>
                        </fieldset>
                        <h3 style="text-align: center;">Range of Interest</h3>
                        <fieldset class="ui-grid-a">
                            <div class="ui-block-a"> 
                                <input type="number" data-mini="true" id="range" name="range" min="0.1" max="5.0" required value="${range}" />
                            </div><div class="ui-block-b">
                                <select name="units" data-mini="true" id="units" value="${rangeUnits}">
                                    <option value="kilometers">km</option>
                                    <option value="miles" selected>mi</option>
                                </select>
                            </div>
                        </fieldset>
                        <input type="submit" name="command" value="Subscribe To Feed"/>
                    </form>
                </div>
            </section>
            <footer data-role="footer">
                <c:url var="nukeSessionActionUrl" value="/entry" />
                <form method="post" action="${nukeSessionActionUrl}">
                    <input type="hidden" name="action" value="nuke"/>
                    <input type="submit" name="cmd" value="Nuke"/>
                </form>
            </footer>
        </div>
        <c:url var="eventSourceUrl" value="/eventstream"/>            
        <script>
            var eventSrcServerUrl = '${eventSourceUrl}';
            // Body Onload:  Detect and initialize the GPS Feature
            $('#location-page').on('pageinit', function() {
                console.log("location page load fired!");
                // setup Events and GPS stuff
                clientPlatformInit(eventSrcServerUrl);

                $(document).on('click', '#clearMessageAreaButton', function() {
                    console.log('onclick clear message button!');
                    $('#eventwindow').val('* cleared *<br>');
                });
                // Bind the Get Location button to its event handlers
                $(document).on('click', '#getPositionButton', function() {
                    console.log('click get position');
                    navigator.geolocation.getCurrentPosition(getLocation, geolocationErrorHandler);
                });

            });
        </script>
    </body>
</html>