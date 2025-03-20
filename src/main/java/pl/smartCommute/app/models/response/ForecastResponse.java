package pl.smartCommute.app.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import pl.smartCommute.app.models.entity.Forecast.Forecast;
import pl.smartCommute.app.models.entity.Forecast.ForecastInformation;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastResponse {
    private Location location;

    private ForecastInfo forecast;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        private String name;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastInfo {
        private List<ForecastDay> forecastday;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastDay {
        private String date;
        private List<ForecastHour> hour;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastHour {
        private String time;
        private double temp_c;
        private double wind_kph;
        private int pressure_mb;
        private int humidity;
        private int cloud;
        private double feelslike_c;
        private int chance_of_rain;
    }

}
