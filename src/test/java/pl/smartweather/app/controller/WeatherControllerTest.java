package pl.smartweather.app.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.smartweather.app.models.entity.Forecast;
import pl.smartweather.app.service.ForecastService;
import pl.smartweather.app.service.WeatherService;
import pl.smartweather.app.utils.TestUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private ForecastService forecastService;

    private static final WireMockServer WIRE_MOCK_SERVER = new WireMockServer(wireMockConfig().dynamicPort());

    @BeforeAll
    public static void setUpWireMockServer() {
        WIRE_MOCK_SERVER.start();
        configureFor("localhost", WIRE_MOCK_SERVER.port());
    }

    @BeforeEach
    public void setupServer() {
        WIRE_MOCK_SERVER.resetAll();
    }

    @DynamicPropertySource
    public static void addDynamicUrl(DynamicPropertyRegistry registry) {
        registry.add("weather.url", WIRE_MOCK_SERVER::baseUrl);
    }

    @Test
    void shouldReturnStatus200withWeatherResponse() throws Exception {
        var responseMock = TestUtils.getJsonResponseFromResource("/responses/WeatherResponse_200.json");

        WIRE_MOCK_SERVER.stubFor(get(urlPathMatching("/.*"))
                .willReturn(ok().withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(responseMock))
                .withQueryParam("key", equalTo("API_KEY"))
                .withQueryParam("q", equalTo("Warsaw")));

        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("key", "API_KEY")
                        .queryParam("q", "Warsaw")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .json(responseMock);
    }

    @Test
    void shouldReturnStatus500WithResponseMessage() throws Exception {
        String message = "Weather API error";

        WIRE_MOCK_SERVER.stubFor(get(urlPathMatching("/.*"))
                .willReturn(serverError()));

        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("key", "API_KEY")
                        .queryParam("q", "Warsaw")
                        .build())
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class).isEqualTo(message);
    }

    @Test
    void shouldReturnStatus200withForecastResponse() throws Exception {
        var forecastResponse = TestUtils.getJsonResponseFromResource("/responses/ForecastResponse_200.json");

        WIRE_MOCK_SERVER.stubFor(get(urlPathMatching("/.*"))
                .withQueryParam("key", equalTo("API_KEY"))
                .withQueryParam("q", equalTo("Warsaw"))
                .withQueryParam("days", equalTo("2"))
                .withQueryParam("aqi", equalTo("false"))
                .withQueryParam("alerts", equalTo("true"))
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(forecastResponse)));

        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/weather/forecast")
                        .queryParam("key", "API_KEY")
                        .queryParam("q", "Warsaw")
                        .queryParam("days", "2")
                        .queryParam("aqi", "false")
                        .queryParam("alerts", "true")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Forecast.class).hasSize(2);
    }

    @Test
    void shouldReturnStatus500WithErrorResponse() {
        String errorResponse = "Forecast API error";
        WIRE_MOCK_SERVER.stubFor(get(urlPathMatching("/.*"))
                .willReturn(serverError()));

        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/weather/forecast")
                        .queryParam("key", "API_KEY")
                        .queryParam("q", "Warsaw")
                        .queryParam("days", "2")
                        .queryParam("aqi", "false")
                        .queryParam("alerts", "true")
                        .build())
                .exchange()
                .expectStatus().is5xxServerError().expectBody(String.class).isEqualTo(errorResponse);

    }
}
