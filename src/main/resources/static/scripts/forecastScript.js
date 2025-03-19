$(document).ready(function () {
    $("#back").click(function (e) {
        window.location.href = "/index.html"
        e.preventDefault();
    });
    var date = new Date();
    date.setDate(date.getDate() + 1);
    let location = sessionStorage.getItem("location");
    let formattedDate = date.toISOString().split("T")[0];
    console.log(location + ", Date:" + formattedDate);
    insertForecast(location, formattedDate);

});
function insertForecast(location, date) {
    console.log(location);
    console.log(date);
    $.ajax({
        type: "GET",
        url: `/weather/forecast/${location}/${date}`,
        dataType: "json",
        success: function (response) {
            document.getElementById('location').textContent = response.location;
            document.getElementById('date').textContent = response.date;
            console.log(response);

            const tableBody = $('#forecast');
            tableBody.empty();

            response.forecast.forEach(forecast => {
                const row = $('<tr></tr>');
                row.append(`<td>${forecast.time}</td>`);
                row.append(`<td>${forecast.temp_c} °C</td>`);
                row.append(`<td>${forecast.feelslike_c} °C</td>`);
                row.append(`<td>${forecast.pressure} hPa</td>`);
                row.append(`<td>${forecast.wind_kph} km/h</td>`);
                row.append(`<td>${forecast.cloud} %</td>`);
                row.append(`<td>${forecast.chance_of_rain} %</td>`);
                row.append(`<td>${forecast.humidity} %</td>`);
                tableBody.append(row);
            });
        }, error: function (xhr, status, error) {
            console.error("Error:", status, error);
            console.log(xhr.responseText);
        }
    })
};