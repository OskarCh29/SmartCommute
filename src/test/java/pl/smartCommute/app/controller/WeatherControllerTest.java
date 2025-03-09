package pl.smartCommute.app.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private static WireMockServer WIRE_MOCK_SERVER;

    @BeforeAll
    public static void setupWireMockServer() {
        WIRE_MOCK_SERVER = new WireMockServer(options().dynamicPort());
        WIRE_MOCK_SERVER.start();
        configureFor("localhost", WIRE_MOCK_SERVER.port());
    }
    @AfterAll
    public static void stopWireMockServer(){
        if(WIRE_MOCK_SERVER != null){
            WIRE_MOCK_SERVER.stop();
        }
    }
    @DynamicPropertySource
    public static void addAPIurl(DynamicPropertyRegistry registry){
        registry.add("weather.url", WIRE_MOCK_SERVER::baseUrl );
    }

}
