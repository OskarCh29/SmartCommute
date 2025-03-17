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
                getForecast(latitude + "," + longitude)
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
            sessionStorage.setItem("location", response.location.name);
        },
        error: function (xhr, status, error) {
            console.error("Error:", status, error);
        }
    });
}

function getWeatherFromLocation(location) {
    document.getElementById('location').textContent = location.location.name;
    document.getElementById('temp').textContent = location.current.temp_c + " (°)"
    document.getElementById('feelsLike').textContent = location.current.feelslike_c + " (°)"
    document.getElementById('wind').textContent = location.current.wind_kph + " km/h"
    document.getElementById('pressure').textContent = location.current.pressure_mb + " hPa"
    document.getElementById('humidity').textContent = location.current.humidity + " %";
    document.getElementById('cloud').textContent = location.current.cloud + " %";

    const currentHour = new Date().getHours();
    const currentLocation = location.location.name;
    checkHistory(currentLocation, currentHour, function (temperatureYesterday) {
        if (temperatureYesterday !== null) {
            document.getElementById('yesterday').textContent =`Yesterday at this time was: ${temperatureYesterday}°`;
        } else {
            document.getElementById('yesterday').textContent = "";
        }
    })
    document.getElementById('yesterday').textContent
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
        saveForDay(0);
        saveForDay(1);
        function saveForDay(dayIndex){
            const forecast = {
                date: response.forecast.forecastday[dayIndex].date,
                location: response.location.name,
                forecast: response.forecast.forecastday[dayIndex].hour.map(hourData => {
                    return {
                        hour: hourData.time,
                        temperature: Math.round(hourData.temp_c),
                        pressure: Math.round(hourData.pressure_mb),
                        wind: Math.round(hourData.wind_kph),
                        cloud: hourData.cloud,
                        rain: hourData.chance_of_rain,
                        humidity: hourData.humidity
                    };
                })
            };
    
            $.ajax({
                type: "POST",
                url: "/weather/forecast",
                data: JSON.stringify(forecast),
                dataType: "json",
                contentType: "application/json",
                success: function (response) {
                    alert(response.message);
                },
                error: function (error) {
                    alert("Error encountered while saving forecast")
                }
    
            });
        }
        
    }
}
function checkHistory(location, currentHour, callback) {
    const currentTime = new Date();
    const formattedDate = new Date(currentTime);

    formattedDate.setDate(formattedDate.getDate() - 1);
    const yesterdayDate = formattedDate.toISOString().split('T')[0];

    $.ajax({
        type: "GET",
        url: `/weather/forecast/${location}/${yesterdayDate}`,
        dataType: "json",
        success: function (response) {
            console.log(response);
            const temperatureYesterday = checkYesterdayTemperature(response, currentHour);
            callback(temperatureYesterday);

        }, error: function (xhr, status, error) {
            console.error("Error encountered: ", status, error)
            alert("Cannot load history weather data");
        }
    });
}
function checkYesterdayTemperature(forecastResponse, currentHour) {
    const yesterdayForecast = forecastResponse.forecast;

    const roundedCurrentHour = Math.round(currentHour);

    const closestHour = yesterdayForecast.find(hour => {
        const forecastTime = new Date(hour.hour);
        return forecastTime.getHours() === roundedCurrentHour;
    });

    if (closestHour) {
        const temperatureYesterday = closestHour.temperature;
        return temperatureYesterday;
    } else {
        console.log("No forecast data available for the same time yesterday.");
        alert("No forecast data available for the same time yesterday.");
        return null;
    }
}



