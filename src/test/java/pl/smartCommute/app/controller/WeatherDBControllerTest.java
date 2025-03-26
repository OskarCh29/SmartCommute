package pl.smartCommute.app.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.smartCommute.app.service.ForecastService;

@WebMvcTest(WeatherController.class)
@ExtendWith(SpringExtension.class)
public class WeatherDBControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ForecastService forecastService;




}
