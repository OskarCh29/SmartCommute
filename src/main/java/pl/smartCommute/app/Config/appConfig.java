package pl.smartCommute.app.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class appConfig {

    @Value("${weather.url}")
    private String weatherApiUrl;

    @Bean
    public WebClient weatherWebClient() {
        return WebClient.builder().baseUrl(weatherApiUrl).build();
    }
}
