package pl.smartCommute.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.smartCommute.app.models.entity.Forecast.Forecast;
import pl.smartCommute.app.models.response.GeneralResponse;
import pl.smartCommute.app.service.ForecastService;
import pl.smartCommute.app.service.WeatherService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final ForecastService forecastService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> getWeather(@RequestParam(required = true, defaultValue = "Warsaw") String location) {
        return weatherService.getWeather(location);
    }

    @GetMapping(value = "/forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> getWeatherForecast(@RequestParam(required = true, defaultValue = "Warsaw") String location) {
        return weatherService.getWeatherForecast(location);
    }

    @GetMapping(value = "/forecast/{location}/{date}")
    public ResponseEntity<Forecast> getForecastByLocationAndDate(@PathVariable String location, @PathVariable String date) {
        Forecast forecast = forecastService.findByLocationAndDate(location, date);
        return ResponseEntity.status(HttpStatus.OK).body(forecast);
    }

    @PostMapping("/forecast")
    public ResponseEntity<GeneralResponse> saveRecord(@RequestBody @Valid Forecast forecast) {
        forecastService.saveForecast(forecast);
        return ResponseEntity.status(HttpStatus.OK).body(new GeneralResponse("Record saved"));
    }
}
