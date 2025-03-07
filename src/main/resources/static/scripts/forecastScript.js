$(document).ready(function () {
    getUserLocation(function(latitude,longitude){
        getWeatherForecast(latitude + "," + longitude)
    });
});
function getWeatherForecast(cords){
    $.ajax({
        type: "GET",
        url: "/weather/forecast",
        data: {location:cords},
        dataType: "json",
        success: function (response) {
            insertForecast(response);
        },error: function (xhr, status, error) {
            console.error("Error loading forecast:", status, error);
        }
    });
}
function insertForecast(forecast){
    const data = new Date();
    document.getElementById('location').textContent = forecast.location.name;
    document.getElementById('date').textContent = "Date:" + data.toLocaleDateString;
    const tableBody=$('#forecast')
    tableBody.empty();
    const hourlyData = forecast.forecast.forecastday[0].hour;

    hourlyData.forEach(hour => {
        const row = $('<tr></tr>');
        const formattedDate = hour.time.split(" ")[1];
        row.append(`<td>${formattedDate}</td>`);
        row.append(`<td>${hour.temp_c} °C</td>`);
        row.append(`<td>${hour.pressure_mb} hPa</td>`);
        row.append(`<td>${hour.wind_kph} km/h</td>`);
        row.append(`<td>${hour.cloud} %</td>`);
        row.append(`<td>${hour.chance_of_rain} %</td>`);
        row.append(`<td>${hour.humidity} %</td>`);
        tableBody.append(row);
    });
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