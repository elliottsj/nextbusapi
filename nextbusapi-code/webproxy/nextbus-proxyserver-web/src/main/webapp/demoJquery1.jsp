<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<c:set var="nbRawApiUrl" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.servletContext.contextPath}/nextbus-rawapi"/>
<!DOCTYPE html>
<html>
    <head>
        <title>JQuery JSON Demo against the nextbus::getRouteConfiguration service method</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="http://code.jquery.com/jquery-latest.js"></script>
    </head>
    <body>
        <div>
            
            <p>This Test JSP Page uses the jQuery AJAX and JSON components to render elements on the page</p>
            <p>Dont forget to View Source to see the full JavaScript client-side implementation.</p>
            <pre style="background-color: tan;">
        &lt;script type="text/javascript"&gt;
            // Lets simulate a Route object for Route 100 on the Boston MBTA
            var route = "{ agency: { id: 'mbta', title: 'Boston MBTA' }, tag: '100', title: 'The 100' } ";
            
            // Use JQuery to invoke the remote servlet
            var example = jQuery.ajax({
                type: 'POST',
                url: '${nbRawApiUrl}',
                data: { method: 'getRouteConfiguration', callParams: route },
                dataType: 'json'
            });
            
            example.error(function() { alert("error in JSON call..."); });
            
            // This Jquery callback simply paints a table.. As the simplest possible demonstration case
            example.done(function(routeConf ) {
                    //  document.write(JSON.stringify(routeConf));  
                    var fgColor = routeConf.uiAdviceColor.hexColor;
                    var bgColor = routeConf.uiAdviceOppositeColor;
                    $('body').append('&lt;table&gt; ');
                    for (i in routeConf.stops) {
                        
                          ... built the tr and td elements in this loop ...
                    }
                    $('body').append('&lt;/table&gt;');
        });
        &lt;/script&gt;
            </pre>
            </p>
        </div>
        <script type="text/javascript"> 
            // Lets simulate a Route object for Route 100 on the Boston MBTA
            var route = "{ agency: { id: 'mbta', title: 'Boston MBTA' }, tag: '100', title: 'The 100' } ";
            
            // Use JQuery to invoke the remote servlet
            var example = jQuery.ajax({
                type: 'POST',
                url: '${nbRawApiUrl}',
                data: { method: 'getRouteConfiguration', callParams: route },
                dataType: 'json'
            });
            
            example.error(function() { alert("error in JSON call..."); });
            
            // This Jquery callback simply paints a table.. As the simplest possible demonstration case
            example.done(function(routeConf ) {
                    //  document.write(JSON.stringify(routeConf));  
                    var fgColor = routeConf.uiAdviceColor.hexColor;
                    var bgColor = routeConf.uiAdviceOppositeColor;
                    $('body').append('<table>');
                    for (i in routeConf.stops) {
                        
                        $('body').append('<tr><td>'+
                        routeConf.stops[i].title+'</td><td>'+routeConf.stops[i].tag+'</td></tr>');
                    }
                    $('body').append('</table>');
        });
        </script>
    </body>
    
    
</html>
