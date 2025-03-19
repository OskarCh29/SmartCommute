package pl.smartCommute.app.models.entity.Forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class ForecastInformation {
    @NotNull(message = "Hour cannot be null")
    @NotBlank(message = "Hour cannot be blank")
    @JsonProperty("time")
    private String hour;

    @NotNull(message = "Temperature field cannot be null")
    @JsonProperty("temp_c")
    private double temperature;

    @NotNull(message = "Pressure cannot be null")
    @JsonIgnoreProperties("pressure_mb")
    private int pressure;

    @NotNull(message = "Wind field cannot be null")
    @JsonProperty("wind_kph")
    private double wind;

    @NotNull(message = "Cloud cannot be null")
    private int cloud;

    @NotNull(message = "Rain cannot be null")
    @JsonProperty("chance_of_rain")
    private int rain;

    @NotNull(message = "Humidity cannot be null")
    private int humidity;

    @NotNull(message = "Feels like cannot be null")
    @JsonProperty("feelslike_c")
    private double  feelsLike;
}
