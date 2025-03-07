package pl.smartCommute.app.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    private final WebClient weatherWebClient;
    private final WebClient forecastWebClient;

    public WeatherService(
            @Qualifier("weatherWebClient") WebClient weatherWebClient,
            @Qualifier("forecastWebClient") WebClient forecastWebClient) {
        this.weatherWebClient = weatherWebClient;
        this.forecastWebClient = forecastWebClient;
    }

    /*
    Check for API error exception later in case of unavailable location
     */
    public Mono<String> getWeather(String location) {
        String query = location.trim();
        return weatherWebClient.get().uri(uriBuilder -> uriBuilder
                        .queryParam("q", query).build()).retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("Api error")))
                .bodyToMono(String.class);
    }

    public Mono<String> getWeatherForecast(String location) {
        String trimmedLocation = location.trim();
        return forecastWebClient.get().uri(uriBuilder -> uriBuilder
                        .queryParam("q", trimmedLocation)
                        .queryParam("days", "1")
                        .queryParam("aqi", false)
                        .queryParam("alerts", true)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("Forecast error")))
                .bodyToMono(String.class);
    }
}
