package pl.smartCommute.app.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WebClient webClient;
    /*
    Check for API error exception later in case of unavailable location
     */
    public Mono<String> getWeather(String location) {
        String query = location.trim();
        return webClient.get().uri(uriBuilder -> uriBuilder
                .queryParam("q", query).build()).retrieve()
                .onStatus(HttpStatusCode::isError,clientResponse -> Mono.error(new RuntimeException("Api error")))
                .bodyToMono(String.class);
    }
}
