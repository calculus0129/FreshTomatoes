package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.model.Movie;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Allows non-static @BeforeAll
class MovieControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(RecControllerTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private LoadMongoDB loadMongoDB;  // Spring injects the LoadMongoDB bean

    @BeforeAll
    public void setUpClass() throws Exception {
        // This will clear existing data and load test data
        loadMongoDB.run();  // Assuming this method can be called directly like this
    }

    @Test
    void testMongoConnection() {
        System.out.println("Using database: " + mongoTemplate.getDb().getName());
        assertThat(mongoTemplate.getDb().getName()).isEqualTo("moviedb-test");
    }

    @Test
    void getAllMovies0() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/movies",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAllMovies1() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                "http://localhost:" + port + "/movies",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // There are 3883 movies
        logger.info(String.format("There are %d movies", Objects.requireNonNull(response.getBody()).size()));
        assertThat(response.getBody()).hasSizeGreaterThan(3000);
    }

    @Test
    void getMovieByMovieId0() {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/movies/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void getMovieByMovieIdInvalid() {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/movies/40001",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // curl -X POST http://localhost:8080/movies -H 'Content-type:application/json' -d '{"movieId":4001, "title":"Ricochet Love", "genres":["Romance","Fantasy"]}'
    @Test
    void createMovie0() {
        // JSON payload as a String
        String jsonPayload = "{\"movieId\":4001, \"title\":\"Ricochet Love\", \"genres\":[\"Romance\",\"Fantasy\"]}";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        // Perform the POST request using restTemplate.exchange
        ResponseEntity<Movie> response = restTemplate.exchange(
                "http://localhost:" + port + "/movies",
                HttpMethod.POST,
                request,
                Movie.class
        );

        // Assertions to verify the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Create expected movie object to compare with the response, using Set.of for genres
        Movie actualMovie = response.getBody(), predictedMovie = new Movie(4001L, "Ricochet Love", Set.of("Romance", "Fantasy"));
        // Assert that the returned movie matches the expected movie
        assertThat(actualMovie)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(predictedMovie);

        // Recursive Comparison: Using usingRecursiveComparison() ensures that
        // even collections within the objects are compared correctly,
        // considering that sets might not maintain a consistent order.
        // Ignoring Fields: The ignoringFields("movieId") call configures AssertJ to ignore the movieId field when performing the equality check.
    }

    /*
        HttpHeaders: This is set to ensure the content type of the request is application/json,
            which is necessary for the server to correctly interpret the incoming data as a JSON object.
        HttpEntity: This encapsulates your JSON payload and headers.
            It represents the full HTTP request body.
        restTemplate.exchange: This method is used for making the request. It's flexible as it allows specifying the HTTP method, which makes it suitable for different kinds of HTTP operations (GET, POST, PUT, DELETE, etc.). In this case, we use HttpMethod.POST.
        ResponseEntity: This is used to capture the response from the server. The generic type (String.class) specifies the expected data type of the response body. If your server sends back a JSON object that maps to a Java class, you might use that class instead of String.
        Assertions: The assertThat from AssertJ is used to assert conditions on the response, such as checking if the HTTP status code is what you expect (e.g., HttpStatus.CREATED if a new resource is successfully created).
     */

    // curl -X POST http://localhost:8080/movies -H 'Content-type:application/json' -d '{"movieId":1, "title":"Ricochet Love", "genres":["Romance","Fantasy"]}'
    @Test
    void createMovieInvalid0() {
        // JSON payload as a String
        String jsonPayload = "{\"movieId\":1, \"title\":\"Ricochet Love\", \"genres\":[\"Romance\",\"Fantasy\"]}";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        // Perform the POST request using restTemplate.exchange
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/movies",
                HttpMethod.POST,
                request,
                String.class
        );

        // Assertions to verify the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    // curl -X PUT http://localhost:8080/movies/1 -H 'Content-type:application/json' -d '{"movieId":1, "title":"Ricochet Love", "genres":["Romance","Fantasy"]}'
    @Test
    void updateMovie0() {
        // {"movieId":1, "title":"Ricochet Love", "genres":["Romance","Fantasy"]}
        String jsonPayload = "{\"movieId\":1, \"title\":\"Ricochet Love\", \"genres\":[\"Romance\", \"Fantasy\"]}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<Movie> response = restTemplate.exchange(
                "http://localhost:" + port + "/movies/1",
                HttpMethod.PUT,
                request,
                Movie.class
        );

        // Assertions to verify the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Create expected movie object to compare with the response, using Set.of for genres
        Movie actualMovie = response.getBody(), predictedMovie = new Movie(1L, "Ricochet Love", Set.of("Romance", "Fantasy"));
        // Assert that the returned movie matches the expected movie
        assertThat(actualMovie)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(predictedMovie);
    }

    @Test
    void updateMovieUnequalMovieInfo() {
        // {"movieId":2, "title":"Ricochet Love", "genres":["Romance","Fantasy"]}
        String jsonPayload = "{\"movieId\":2, \"title\":\"Ricochet Love\", \"genres\":[\"Romance\", \"Fantasy\"]}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/movies/1",
                HttpMethod.PUT,
                request,
                String.class
        );

        // Assertions to verify the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void updateMovieNotFound() {
        // {"movieId":4002, "title":"Ricochet Love", "genres":["Romance","Fantasy"]}
        String jsonPayload = "{\"movieId\":4002, \"title\":\"Ricochet Love\", \"genres\":[\"Romance\", \"Fantasy\"]}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/movies/4002",
                HttpMethod.PUT,
                request,
                String.class
        );

        // Assertions to verify the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // curl -X DELETE http://localhost:8080/movies/2
    @Test
    void deleteMovie0() {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/movies/2",
                HttpMethod.DELETE,
                null,
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // curl -X DELETE http://localhost:8080/movies/4002
    @Test
    void deleteMovie1() {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/movies/4002",
                HttpMethod.DELETE,
                null,
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}