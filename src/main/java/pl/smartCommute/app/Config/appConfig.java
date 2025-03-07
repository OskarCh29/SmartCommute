package pl.smartCommute.app.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class appConfig {

    @Value("${weather.current}")
    private String weatherApiUrl;

    @Value("${weather.forecast}")
    private String weatherForecastUrl;

    @Bean
    public WebClient weatherWebClient() {
        return WebClient.builder().baseUrl(weatherApiUrl).build();
    }
    @Bean
    public WebClient forecastWebClient(){
        return WebClient.builder().baseUrl(weatherForecastUrl).build();
    }
}
