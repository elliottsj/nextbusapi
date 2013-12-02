
function setupServerSideEventListener(eventSrcServerUrl) {

    // Yes! Server-sent events support!  
    source = new EventSource(eventSrcServerUrl);
    if (source === null) {
        $('#warnings').text('Unable to create Event Source listener.<br>');
        return;
    }
    $('warnings').append('** Event Source Created.<br>');
    console.log('event source created');
    // register event listeners
    source.addEventListener('message', function(sse) {
        $('#eventwindow').prepend(sse.data);
    }, false);

    source.addEventListener('error', function(sse) {
        if (sse.readyState === EventSource.CLOSED) {
            $('#warnings').text('Server side event source has permanently closed.<br>');
        }
    }, false);
    source.addEventListener('open', function(sse) {
        console.log('event source connection opened.');
        $('#warnings').text('Event source opened.');
    }, false);
}

// Client Browser sense
function getLocation(position) {
    console.log('getLocation2()');
    if (position === null || position.coords === null) return;
    $('#latitude').val(position.coords.latitude);
    $('#longitude').val(position.coords.longitude);
    if (position.coords.accuracy < 1.0) {
        $('#warnings').text('Low GPS Accuracy -  ' + position.coords.accuracy + ' kilometers!');
    } else {
        $('#warnings').text('Set position.');
    }
}

// Deal with GPS subsystem errors
function geolocationErrorHandler(error) {
    switch (error.code) {
        case error.PERMISSION_DENIED:
            $('#warnings').text('GPS Permission Denied. ');
            break;
        case error.POSITION_UNAVAILABLE:
            $('#warnings').text('GPS Position Report Unavailable.');
            break;
        case error.TIMEOUT:
            $('#warnings').text('GPS Position Request Timeout.');
            break;
    }
}

function clientPlatformInit(eventSrcServerUrl) {
    // Detect HTML5 API Support and warn if required client functions are not avail.
    // Try HTML5 Geolocation Service.
    if (!"geolocation" in navigator) {
        $('#warnings').text('HTML 5 Geolocation feature not present.');
    } else {
        // bind button event handlers to geolocation
    }
    if (!window.EventSource) {
         $('#warnings').text('HTML 5 Server Side Events feature not present.');
    } else {
        setupServerSideEventListener(eventSrcServerUrl);
        console.log("setup sse event handlers");
    }
}
     
            