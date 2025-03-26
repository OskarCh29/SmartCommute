/*
Providing access to API by KEY located in config.json
Example:
{
  "API_KEY": "YOUR KEY"
}
*/
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

            getUserCoordinates(function (latitude, longitude) {
                initMapWithUserLocation(apiKey, latitude, longitude);
                initWeather(latitude + "," + longitude);
                getForecast(latitude + "," + longitude);
            });
        })
        .catch(error => console.error("No API KEY PROVIDED", error));
});
function getUserCoordinates(callback){
    let coords = sessionStorage.getItem("coords");
    if(coords !== null){
        let parsedCoords = JSON.parse(coords);
        callback(parsedCoords.lat, parsedCoords.lng);
    }else{
        getUserLocation(callback);
    }
}

function initMapWithUserLocation(apiKey, lat, lng) {
    var platform = new H.service.Platform({
        apikey: apiKey
    });
    var defaultLayers = platform.createDefaultLayers();

    var mapZoom = sessionStorage.getItem("mapZoom");
    let mapCenter = sessionStorage.getItem("mapCenter");

    var zoom = mapZoom ? parseInt(mapZoom) : 12;
    var center = mapCenter ? JSON.parse(mapCenter) : { lat: lat, lng: lng };


    var map = new H.Map(
        document.getElementById('map'),
        defaultLayers.vector.normal.map,
        {
            zoom: zoom,
            center: center
        }
    );

    var ui = H.ui.UI.createDefault(map, defaultLayers);
    var behavior = new H.mapevents.Behavior(new H.mapevents.MapEvents(map));

    map.addEventListener('dbltap', function (evt) {
        var pointer = evt.currentPointer;
        if (!pointer) return;
        var coords = map.screenToGeo(pointer.viewportX, pointer.viewportY);
        console.log("Coords:" + coords.lat, coords.lng);
        initWeather(coords.lat + "," + coords.lng);
        getForecast(coords.lat + "," + coords.lng);

    });
    map.addEventListener('drag', function () {
        var center = map.getCenter();
        var zoom = map.getZoom();
        sessionStorage.setItem("mapCenter", JSON.stringify({ lat: center.lat, lng: center.lng }));
        sessionStorage.setItem("mapZoom", zoom);
    });

}
function initWeather(coordinates) {
    $.ajax({
        type: "GET",
        url: "/weather",
        dataType: "json",
        data: { location: coordinates },
        success: function (response) {
            getWeatherFromLocation(response);
            sessionStorage.setItem("location", response.location.name);
            sessionStorage.setItem("coords", JSON.stringify({lat: response.location.lat, lng: response.location.lon}));
        },
        error: function (status, error) {
            console.error("Error:", status, error);
        }
    });
}

function getWeatherFromLocation(weather) {
    document.getElementById('location').textContent = weather.location.name;
    document.getElementById('temp').textContent = weather.current.temp_c + " °"
    document.getElementById('feelsLike').textContent = weather.current.feelslike_c + " °"
    document.getElementById('wind').textContent = weather.current.wind_kph + " km/h"
    document.getElementById('pressure').textContent = weather.current.pressure_mb + " hPa"
    document.getElementById('humidity').textContent = weather.current.humidity + " %";
    document.getElementById('cloud').textContent = weather.current.cloud + " %";

    const currentLocation = weather.location.name;
    checkHistory(currentLocation)
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
function getForecast(location) {

    $.ajax({
        type: "GET",
        url: "/weather/forecast",
        data: { location: location },
        dataType: "json",
        success: function (response) {
            console.log(response);
            saveForecast(response);
        }, error: function (xhr, status, error) {
            console.error("Error loading forecast:", status, error);
        }
    });
    function saveForecast(response) {
        response.forEach(dayForecast => {
            $.ajax({
                type: "POST",
                url: "/weather/forecast",
                data: JSON.stringify(dayForecast),
                dataType: "json",
                contentType: "application/json",
                success: function (response) {
                    console.log(response.message);
                },
                error: function (response) {
                    console.log(response.message)
                }

            });
        });
    }

}
function checkHistory(location) {
    const currentTime = new Date();
    const currentMinutes = currentTime.getMinutes();

    let timeNow = currentTime.getHours();
    if (currentMinutes >= 30) {
        timeNow += 1;
    }
    timeNow = Math.min(timeNow,23);
    const formattedDate = new Date(currentTime);
    formattedDate.setDate(formattedDate.getDate() - 1);
    const yesterdayDate = formattedDate.toISOString().split('T')[0];
    console.log(timeNow)

    $.ajax({
        type: "GET",
        url: `/weather/forecast/${location}/${yesterdayDate}`,
        dataType: "json",
        success: function (response) {
            console.log(response);
            checkYesterdayTemperature(response,timeNow);

        }, error: function () {
            document.getElementById('yesterday').textContent = "";
        }
    });
}
function checkYesterdayTemperature(forecastResponse, timeNow) {
    const yesterdayForecast = forecastResponse.forecast;

    const closestHour = yesterdayForecast.find(forecastHourData => {
        const [forecastHour] = forecastHourData.hour.split(':').map(Number);
        return forecastHour === timeNow
    })

    if (closestHour) {
        const temperatureYesterday = closestHour.temperature;
        document.getElementById('yesterday').textContent = "Yesterday at this time was " + temperatureYesterday +" °" ;
        
    } else {
        console.log("No forecast data available for the same time yesterday.");
        document.getElementById('yesterday').textContent = "";
    }
}



