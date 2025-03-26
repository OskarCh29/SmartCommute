package pl.smartweather.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smartweather.app.models.entity.Forecast;

import java.util.Optional;

@Repository
public interface ForecastRepository extends MongoRepository<Forecast,String> {
    Optional<Forecast> findByLocationAndDate(String location, String date);
}
