package pl.smartCommute.app.Entity.Weather;

import lombok.Data;

@Data
public class Weather {

    private Location location;

    private CurrentWeather current;
}
