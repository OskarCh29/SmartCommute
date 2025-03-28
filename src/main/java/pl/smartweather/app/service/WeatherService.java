package pl.smartweather.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.smartweather.app.exception.InternalServerException;
import pl.smartweather.app.models.entity.Forecast;
import pl.smartweather.app.models.entity.ForecastInformation;
import pl.smartweather.app.models.response.ForecastResponse;
import pl.smartweather.app.models.response.WeatherResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${security.API_KEY}")
    private String API_KEY;

    private final WebClient webClient;

    /*
    Check for API error exception later in case of unavailable location
     */
    public Mono<WeatherResponse> getWeather(String location) {
        String query = location.trim();
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/v1/current.json")
                        .queryParam("key", API_KEY)
                        .queryParam("q", query)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new InternalServerException("Weather API error")))
                .bodyToMono(WeatherResponse.class);
    }

    public Mono<List<Forecast>> getWeatherForecast(String location) {
        String trimmedLocation = location.trim();
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast.json")
                        .queryParam("key", API_KEY)
                        .queryParam("q", trimmedLocation)
                        .queryParam("days", "2")
                        .queryParam("aqi", false)
                        .queryParam("alerts", true)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        Mono.error(new InternalServerException("Forecast API error")))
                .bodyToMono(ForecastResponse.class)
                .map(this::mapResponseToForecast);

    }
    private List<Forecast> mapResponseToForecast(ForecastResponse response) {
        return response.getForecast().getForecastday().stream()
                .map(forecastDay -> new Forecast(
                        null,
                        forecastDay.getDate(),
                        response.getLocation().getName(),
                        forecastDay.getHour().stream()
                                .map(hour -> new ForecastInformation(
                                        hour.getTime().split(" ")[1],
                                        hour.getTemp_c(),
                                        hour.getPressure_mb(),
                                        hour.getWind_kph(),
                                        hour.getCloud(),
                                        hour.getChance_of_rain(),
                                        hour.getHumidity(),
                                        hour.getFeelslike_c()
                                ))
                                .toList()
                ))
                .toList();
    }
}
