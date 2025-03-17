package pl.smartCommute.app.models.entity.Forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class ForecastInformation {
    @NotNull(message = "Hour cannot be null")
    @NotBlank(message = "Hour cannot be blank")
    private String hour;

    @NotNull(message = "Temperature field cannot be null")
    private int temperature;

    @NotNull(message = "Pressure cannot be null")
    private int pressure;

    @NotNull(message = "Wind field cannot be null")
    private int wind;

    @NotNull(message = "Cloud cannot be null")
    private int cloud;

    @NotNull(message = "Rain cannot be null")
    private int rain;

    @NotNull(message = "Humidity cannot be null")
    private int humidity;

    @NotNull(message = "Feels like cannot be null")
    private int feelsLike;
}
