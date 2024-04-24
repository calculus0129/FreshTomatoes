package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.model.Movie;
import com.example.App.movieinfo.model.Rating;
import com.example.App.movieinfo.repository.LoadMongoDB;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Allows non-static @BeforeAll
class RatingControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(RatingControllerTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LoadMongoDB loadMongoDB;  // Spring injects the LoadMongoDB bean

    @BeforeAll
    public void setUpClass() throws Exception {
        // This will clear existing data and load test data
        loadMongoDB.run();
    }

    // curl http://localhost:8080/ratings?max=10
    @Test
    void getAllRatings0() {
        ResponseEntity<List<Rating>> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings?max=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    // curl http://localhost:8080/ratings
    @Test
    void getAllRatings1() {
        ResponseEntity<List<Rating>> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // curl "http://localhost:8080/ratings?movieId=1&max=5"
    @Test
    void getRatingsByMovieId0() {
        ResponseEntity<List<Rating>> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings?movieId=1&max=5",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // curl "http://localhost:8080/ratings?movieId=1"
    @Test
    void getRatingsByMovieId1() {
        ResponseEntity<List<Rating>> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings?movieId=1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    // curl "http://localhost:8080/ratings?movieId=1&max=5"
    @Test
    void getRatingsByMovieId2() {
        ResponseEntity<List<Rating>> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings?movieId=1&max=-1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getRatingByUserIdAndMovieId0() {
        ResponseEntity<Optional<Rating>> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings?userId=6&movieId=1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void getRatingByUserIdAndMovieId1() {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings?userId=7000&movieId=1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // curl -X PUT "http://localhost:8080/ratings?userId=6&movieId=1" -H "Content-Type: application/json" -d '{"rating": 3, "timestamp": 999999999}'
    @Test
    void updateRating0() {
        // JSON payload as a String
        String jsonPayload = "{\"rating\":3, \"timestamp\":999999999}";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings?userId=6&movieId=1",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // curl -X PUT "http://localhost:8080/ratings?userId=7000&movieId=1" -H "Content-Type: application/json" -d '{"rating": 3, "timestamp": 999999999}'
    @Test
    void updateRating1() {
        // JSON payload as a String
        String jsonPayload = "{\"rating\":3, \"timestamp\":999999999}";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings?userId=7000&movieId=1",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // curl -X PUT "http://localhost:8080/ratings?userId=1&movieId=7000" -H "Content-Type: application/json" -d '{"rating": 3, "timestamp": 999999999}'
    @Test
    void updateRating2() {
        // JSON payload as a String
        String jsonPayload = "{\"rating\":3, \"timestamp\":999999999}";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings?userId=1&movieId=7000",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    // curl -X PUT "http://localhost:8080/ratings?userId=2&movieId=1" -H "Content-Type: application/json" -d '{"rating": 3, "timestamp": 999999999}'
    @Test
    void updateRating3() {
        // JSON payload as a String
        String jsonPayload = "{\"rating\":3, \"timestamp\":999999999}";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings?userId=2&movieId=1",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // curl -X POST "http://localhost:8080/ratings" -H "Content-Type: application/json" -d '{"userId":7001, "movieId":1, "rating": 5, "timestamp": 922222222}'
    @Test
    void createRating0() {
        // JSON payload as a String
        String jsonPayload = "{\"userId\":7001, \"movieId\":1, \"rating\": 5, \"timestamp\":922222222}";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
        ResponseEntity<Rating> response = restTemplate.exchange(
                "http://localhost:"+port+"/ratings",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Rating actualRating = response.getBody(), expectedRating = new Rating(7001L, 1L, 5L, 922222222L);
        assertThat(actualRating)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedRating);
    }

    // curl -X POST "http://localhost:8080/ratings" -H "Content-Type: application/json" -d '{"userId":6, "movieId":1, "rating": 5, "timestamp": 922222222}'
    @Test
    void createRating1() {
        // JSON payload as a String
        String jsonPayload = "{\"userId\":6, \"movieId\":1, \"rating\": 5, \"timestamp\":922222222}";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:"+port+"/ratings",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    // curl -X DELETE "http://localhost:8080/ratings?userId=8&movieId=1"
    @Test
    void deleteRating0() {
        // Build the request
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:"+port+"/ratings?userId=8&movieId=1",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // curl -X DELETE "http://localhost:8080/ratings?userId=7002&movieId=1"
    @Test
    void deleteRating1() {
        // Build the request
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:"+port+"/ratings?userId=7002&movieId=1",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}