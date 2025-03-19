package pl.smartCommute.app.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smartCommute.app.exception.RecordExistsException;
import pl.smartCommute.app.models.entity.Forecast.Forecast;
import pl.smartCommute.app.repository.ForecastRepository;

@Service
@RequiredArgsConstructor
public class ForecastService {

    private final ForecastRepository forecastRepository;


    public Forecast saveForecast(Forecast forecast) {
        if (findByLocationAndDate(forecast.getLocation(), forecast.getDate()) != null) {
            throw new RecordExistsException("Record exists");
        }
        return forecastRepository.save(forecast);
    }

    public Forecast findByLocationAndDate(String location, String date) {
        return forecastRepository.findByLocationAndDate(location, date).orElse(null);
    }

}
