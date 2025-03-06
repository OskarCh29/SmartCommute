package pl.smartCommute.app.Entity.Weather;

import lombok.Data;

@Data
public class Location {

    private String name;
    private String country;
    private double latitude;
    private double longitude;
    private String tz_id;
    private String localTime;

}
