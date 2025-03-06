$(document).ready(function () {
    fetch('/config.json')
        .then(response => {
            if (!response.ok) {
                throw new Error("No config file with API key");
            }
            return response.json();
        })
        .then(config => {
            if (!config.API_KEY) {
                throw new Error("No API key in config file");
            }

            const apiKey = config.API_KEY;
            getUserLocation(function (latitude, longitude) {
                initMapWithUserLocation(apiKey, latitude, longitude);
                initWeather(latitude + "," + longitude);
                initDailyForecast(latitude + "," + longitude);
            });
        })
        .catch(error => console.error("No API KEY PROVIDED", error));
});

function initMapWithUserLocation(apiKey, lat, lng) {
    var platform = new H.service.Platform({
        apikey: apiKey
    });
    var defaultLayers = platform.createDefaultLayers();

    var map = new H.Map(
        document.getElementById('map'),
        defaultLayers.vector.normal.map,
        {
            zoom: 12,
            center: { lat: lat, lng: lng }
        }
    );

    var ui = H.ui.UI.createDefault(map, defaultLayers);
    var behavior = new H.mapevents.Behavior(new H.mapevents.MapEvents(map));
}

function initWeather(coordinates) {
    $.ajax({
        type: "GET",
        url: "/weather",
        dataType: "json",
        data: { location: coordinates },
        success: function (response) {
            getWeatherFromLocation(response);
        },
        error: function (xhr, status, error) {
            console.error("Error:", status, error);
        }
    });
}

function getWeatherFromLocation(location) {
    document.getElementById('location').textContent = location.location.name;
    document.getElementById('temp').textContent = location.current.temp_c;
    document.getElementById('wind').textContent = location.current.wind_kph;
    document.getElementById('pressure').textContent = location.current.pressure_mb;
    document.getElementById('humidity').textContent = location.current.humidity + "%";
    document.getElementById('cloud').textContent = location.current.cloud + "%";
}
function getUserLocation(callback) {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            let latitude = position.coords.latitude;
            let longitude = position.coords.longitude;
            callback(latitude, longitude);
        }, function (error) {
            console.error("Cannot load location", error);
            alert("Cannot load user location");
        });
    } else {
        console.log("Geolocation not supported by this browser");
    }
}
setInterval(getCurrentTime, 1000);
function getCurrentTime() {
    const currentDate = new Date();
    document.getElementById('date').textContent = "Date & Time: " + currentDate.toLocaleString();
}