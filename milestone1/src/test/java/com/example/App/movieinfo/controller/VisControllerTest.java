package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.repository.CsvFileGenerator;
import com.example.App.movieinfo.repository.LoadMongoDB;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Allows non-static @BeforeAll
public class VisControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(RecControllerTest.class);

    @Mock
    private CsvFileGenerator csvGenerator;

    @Autowired
    @InjectMocks
    private VisController controller;

    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private LoadMongoDB loadMongoDB;  // Spring injects the LoadMongoDB bean

    @BeforeAll
    public void setUpClass() throws Exception {
        // This will clear existing data and load test data
        loadMongoDB.run();  // Assuming this method can be called directly like this
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
    /*
    /genres GET RestAPI
     */
    @Test
    public void testVisGenreByF() throws Exception {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/movieInfo/genres?s=F",
                List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void testVisGenreByB() throws Exception {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/movieInfo/genres?s=B",
                List.class);

        assertThat(response.getBody()).isEmpty();
    }
    /*
    /titles GET RestAPI
     */
    @Test
    public void testVisTitleByToySto() throws Exception {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/movieInfo/titles?s=Toy%20Sto",
                List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void testVisTitleByA() throws Exception {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/movieInfo/titles?s=A",
                List.class);

        assertThat(response.getBody().size()).isEqualTo(4);
    }
    /*
    /csv/timeline GET RestAPI
     */
    @Test
    public void testVisCsvTimeline1() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        controller.exportIntoCSVtimelines(response);
        assertEquals("text/csv", response.getContentType());
    }
    @Test
    public void testVisCsvTimeline2() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        controller.exportIntoCSVtimelines(response);
        assertEquals(200, response.getStatus());
    }
    /*
    /csv/timeline/one GET RestAPI
     */
    @Test
    public void testVisCsvTimelineByGenre() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        controller.exportIntoCSVtimeline(response, "Sci-Fi");
        assertEquals("text/csv", response.getContentType());
    }
    @Test
    public void testVisCsvTimelineByNotGenre() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        Assertions.assertThrows(MovieNotFoundException.class, () -> {
            controller.exportIntoCSVtimeline(response, "Sci");
        });
    }
    /*
    /csv/year GET RestAPI
     */
    @Test
    public void testVisCsvYearByNull() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        Assertions.assertThrows(MovieNotFoundException.class, () -> {
            controller.exportIntoCSVyear(response, null, null);
        });
    }
    @Test
    public void testVisCsvYearByYear() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        controller.exportIntoCSVyear(response, 1950, null);
        assertEquals("text/csv", response.getContentType());
    }
    @Test
    public void testVisCsvYearByNotYear() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        Assertions.assertThrows(MovieNotFoundException.class, () -> {
            controller.exportIntoCSVyear(response, 1, null);
        });
    }
    @Test
    public void testVisCsvYearByTitle() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        controller.exportIntoCSVyear(response, null, "Toy Story (1995)");
        assertEquals(null, response.getContentType());
    }
    @Test
    public void testVisCsvYearByNotTitle() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        Assertions.assertThrows(MovieNotFoundException.class, () -> {
            controller.exportIntoCSVyear(response, null, "sdfsdf");
        });
    }
}
