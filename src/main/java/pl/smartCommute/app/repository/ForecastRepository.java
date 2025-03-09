package pl.smartCommute.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smartCommute.app.models.entity.Forecast.Forecast;

import java.util.Optional;

@Repository
public interface ForecastRepository extends MongoRepository<Forecast,String> {
    Optional<Forecast> findByLocationAndDate(String location, String date);
}
