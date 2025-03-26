package pl.smartweather.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.smartweather.app.models.entity.Forecast;
import pl.smartweather.app.models.response.WeatherResponse;
import pl.smartweather.app.service.WeatherService;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<WeatherResponse> getWeather(@RequestParam(required = true, defaultValue = "Warsaw") String location) {
        return weatherService.getWeather(location);
    }

    @GetMapping(value = "/forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Forecast>> getWeatherForecast(@RequestParam(required = true, defaultValue = "Warsaw") String location) {
        return weatherService.getWeatherForecast(location);
    }


}
