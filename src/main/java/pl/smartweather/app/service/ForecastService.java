package pl.smartweather.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smartweather.app.exception.RecordExistsException;
import pl.smartweather.app.exception.RecordNotFoundException;
import pl.smartweather.app.models.entity.Forecast;
import pl.smartweather.app.repository.ForecastRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ForecastService {

    private final ForecastRepository forecastRepository;

    /*
    Add some exception handling later
     */

    public Forecast saveForecast(Forecast forecast) {
        Optional<Forecast> record = forecastRepository.findByLocationAndDate(forecast.getLocation(), forecast.getDate());
        if (record.isPresent()) {
            throw new RecordExistsException("Record already exists");
        } else {
            return forecastRepository.save(forecast);

        }
    }
    public Forecast findByLocationAndDate(String location, String date) {
        return forecastRepository.findByLocationAndDate(location, date).orElseThrow(
                () -> new RecordNotFoundException("No record found"));
    }

}
