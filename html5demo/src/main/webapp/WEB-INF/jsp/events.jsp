<%@ page contentType="text/html" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="css" value="/css/html5demo.css" />
<!DOCTYPE html>
<html>
    <head>
        <title>Events</title>
        <meta charset="utf-8">
        <meta http-equiv="cache-control" content="max-age=0" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta http-equiv="expires" content="0" />
        <meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
        <meta http-equiv="pragma" content="no-cache" />
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css" />
        <style>

        </style>
        <script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
        <script src="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js"></script>           
    </head>
    <body>
        <div id="events-page" data-role="page">
            <header data-role="header">
                <div data-role="navbar">
                    <ul> 
                        <c:url var="locationUrl" value="/navigator" ><c:param name="to" value="location"/></c:url>
                        <li><a href="${locationUrl}" data-ajax="false" data-icon="home">Location</a></li>
                        <li><a href="#" data-icon="home" class="ui-btn-active">Events</a></li>
                            <c:url var="infoUrl" value="/navigator"><c:param name="to" value="info"/></c:url>
                        <li><a href="${infoUrl}" data-icon="home">Info</a></li>
                    </ul>
                </div>
                <div id="warnings"></div>
            </header>
            <section data-role="section">
                <div  id="eventstream-control" class="ui-grid-a">
                    <button id="clearMessageAreaButton">Clear</button>
                    <textarea id="eventwindow" data-role="none" rows="8" >
                    </textarea>
                </div>
            </section>
            <footer data-role="footer">
            </footer>
        </div>
        <script>
            console.log("BOOM!");
        </script>
    </body>
</html>
