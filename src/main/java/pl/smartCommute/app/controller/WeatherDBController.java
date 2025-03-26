package pl.smartCommute.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.smartCommute.app.models.entity.Forecast.Forecast;
import pl.smartCommute.app.models.response.GeneralResponse;
import pl.smartCommute.app.service.ForecastService;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherDBController {

    private final ForecastService forecastService;

    @PostMapping("/forecast")
    public ResponseEntity<GeneralResponse> saveRecord(@RequestBody @Valid Forecast forecast) {
        forecastService.saveForecast(forecast);
        return ResponseEntity.status(HttpStatus.OK).body(new GeneralResponse("Record saved"));
    }
    @GetMapping(value = "/forecast/{location}/{date}")
    public ResponseEntity<Forecast> getForecastByLocationAndDate(@Valid @PathVariable String location, @Valid @PathVariable String date) {
        Forecast forecast = forecastService.findByLocationAndDate(location, date);
        return ResponseEntity.status(HttpStatus.OK).body(forecast);
    }
}
