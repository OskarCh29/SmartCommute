package pl.smartweather.app.models.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "forecastHistory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Forecast {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotNull(message = "Date cannot be null")
    @NotBlank(message = "Date missing")
    private String date;

    @Indexed(unique = true)
    @NotNull(message = "Location cannot be null")
    @NotBlank(message = "Location missing")
    private String location;

    @NotEmpty
    private List<ForecastInformation> forecast;
}
