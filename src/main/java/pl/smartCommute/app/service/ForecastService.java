package pl.smartCommute.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smartCommute.app.models.entity.Forecast.Forecast;
import pl.smartCommute.app.repository.ForecastRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ForecastService {

    private final ForecastRepository forecastRepository;

    /*
    Handle the specific exception - later
     */
    public Forecast saveForecast(Forecast forecast){
        if(findByLocationAndDate(forecast.getLocation(),forecast.getDate()) != null){
            throw new RuntimeException("Record already exists");
        }
        return forecastRepository.save(forecast);
    }

    public Forecast findByLocationAndDate(String location, String date){
        return forecastRepository.findByLocationAndDate(location,date).orElseThrow(()-> new RuntimeException("Record not exists"));
    }

}
