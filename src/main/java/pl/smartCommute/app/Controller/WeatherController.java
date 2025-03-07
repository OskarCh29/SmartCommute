package pl.smartCommute.app.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.smartCommute.app.Service.WeatherService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> getWeather(@RequestParam(required = true, defaultValue = "Warsaw") String location) {
        return weatherService.getWeather(location);
    }
}
