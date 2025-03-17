$(document).ready(function () {
    $("#back").click(function (e) {
        window.location.href = "/index.html"
        e.preventDefault();
    });
    var date = new Date();
    date.setDate(date.getDate() + 1);
    let location = sessionStorage.getItem("location");
    let formattedDate = date.toISOString().split("T")[0];
    insertForecast(location, formattedDate);

});
function insertForecast(location, date) {
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

            response.forecast.forEach(hour => {
                const row = $('<tr></tr>');
                const formattedDate = hour.hour.split(" ")[1];
                row.append(`<td>${formattedDate}</td>`);
                row.append(`<td>${hour.temperature} °C</td>`);
                row.append(`<td>${hour.feelsLike} °C</td>`);
                row.append(`<td>${hour.pressure} hPa</td>`);
                row.append(`<td>${hour.wind} km/h</td>`);
                row.append(`<td>${hour.cloud} %</td>`);
                row.append(`<td>${hour.rain} %</td>`);
                row.append(`<td>${hour.humidity} %</td>`);
                tableBody.append(row);
            });
        }
    })
};