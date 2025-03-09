$(document).ready(function () {
    $("#back").click(function (e) {
        window.location.href = "/index.html"
        e.preventDefault();
    });
    var date = new Date();
    date.setDate(date.getDate() + 1);
    const formattedDate = date.toISOString().split("T")[0];
    let location = sessionStorage.getItem(location);
    getForecast(lo, formattedDate);
});
function getForecast(location, date) {
    $.ajax({
        type: "GET",
        url: `/forecast/${location}/${date}`,
        dataType: "json",
        success: function (response) {
            insertForecast(response)
        }, error: function (error) {
            alert("No forecast found")
        }
    });
}
function insertForecast(forecast) {
    document.getElementById('location').textContent = forecast.location;
    document.getElementById('date').textContent = forecast.date
    
    const tableBody = $('#forecast tbody')
    tableBody.empty();

    const hourlyData = forecast.forecast

    hourlyData.forEach(hour => {
        const row = $('<tr></tr>');
        const formattedDate = hour.time.split(" ")[1];
        row.append(`<td>${formattedDate}</td>`);
        row.append(`<td>${hour.temperature} °C</td>`);
        row.append(`<td>${hour.pressure} hPa</td>`);
        row.append(`<td>${hour.wind} km/h</td>`);
        row.append(`<td>${hour.cloud} %</td>`);
        row.append(`<td>${hour.rain} %</td>`);
        row.append(`<td>${hour.humidity} %</td>`);
        tableBody.append(row);
    });
}