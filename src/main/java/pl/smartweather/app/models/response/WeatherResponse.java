package pl.smartweather.app.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    private Location location;

    private CurrentWeather current;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location{
        private String name;
        private String lat;
        private String lon;
    }
    public static class CurrentWeather{

        @JsonProperty("temp_c")
        private double temperature;

        @JsonProperty("feelslike_c")
        private double feelsLike;

        @JsonProperty("wind_kph")
        private double wind;

        @JsonProperty("pressure_mb")
        private double pressure;

        @JsonProperty("humidity")
        private int humidity;

        @JsonProperty("cloud")
        private int cloud;
    }
}
