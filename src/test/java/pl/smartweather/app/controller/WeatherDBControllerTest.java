package pl.smartweather.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.smartweather.app.models.entity.Forecast;
import pl.smartweather.app.service.ForecastService;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
@ExtendWith(SpringExtension.class)
public class WeatherDBControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ForecastService forecastService;

    @Test
    void PostMappingShouldReturnStatus200andResponse() throws Exception {
        Forecast forecast = new Forecast();
        forecast.setLocation("TestLocation");
        forecast.setDate("Today-Test");
        forecast.setForecast(new ArrayList<>());

        when(forecastService.saveForecast(any(Forecast.class))).thenReturn(forecast);

        mockMvc.perform(post("/weather/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(forecast))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Record saved"));
        verify(forecastService.saveForecast(forecast));
    }


}
