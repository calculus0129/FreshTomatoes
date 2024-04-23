package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.model.Movie;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
class MovieControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(RecControllerTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;

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

//    @Test
//    void getMoviesByAverageRating0() {
//        ResponseEntity<List<Movie>> response = restTemplate.exchange(
//                "http://localhost:" + port + "/movies/averageRating/5",
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<>() {}
//        );
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//    @Test
//    void getMoviesByAverageRatingInvalidOnes() {
//        ResponseEntity<String> response = restTemplate.exchange(
//                "http://localhost:" + port + "/movies/averageRating/0",
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<>() {}
//        );
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        response = restTemplate.exchange(
//                "http://localhost:" + port + "/movies/averageRating/6",
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<>() {}
//        );
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//    }

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

    @Test
    void createMovie() {
    }

    @Test
    void updateMovie() {
    }

    @Test
    void deleteMovie() {
    }
}