package pl.smartCommute.app.Entity.Weather;

import lombok.Data;

@Data
public class CurrentWeather {

    private String last_updated;
    private double temp_c;
    private double wind_kph;
    private double pressure_mb;
    private double humidity;
    private double cloud;
    private double feelslike_c;
    private double vis_km;
    private double gust_kph;
}
