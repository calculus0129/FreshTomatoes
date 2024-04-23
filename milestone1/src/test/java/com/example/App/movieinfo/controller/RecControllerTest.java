package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.model.*;
import com.example.App.movieinfo.repository.LoadMongoDB;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Allows non-static @BeforeAll
class RecControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(RecControllerTest.class);

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
    }

    @Test
    public void testRecByUserValid() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/recommend/user?gender=F&age=18&occ=20",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testRecByUserInvalidGender() throws Exception {
        for(char c='a';c<='z';++c) {
            assertThat(restTemplate.getForEntity(
                    "http://localhost:" + port + String.format("/recommend/user?gender=%c&age=18&occ=20", c),
                    String.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }
    @Test
    public void testRecByUserInvalidOccupation() throws Exception {
        assertThat(restTemplate.getForEntity(
        "http://localhost:" + port + "/recommend/user?gender=F&age=18&occ=21",
        String.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(restTemplate.getForEntity(
                "http://localhost:" + port + "/recommend/user?gender=F&age=18&occ=121",
                String.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // Takes about 1'15".
//    @Test
//    @DisplayName("Testing for all valid inputs")
//    public void testRecByUserAge18() throws Exception {
//        int movieMin = Integer.MAX_VALUE;
//        /*
//        exchange method is used instead of getForEntity for better control over the request and to specify the type reference explicitly.
//        HttpMethod.GET is specified as the HTTP method.
//        new ParameterizedTypeReference<List<Movie>>() {} maintains generic type information during runtime, enabling the RestTemplate to convert the incoming JSON into a List<Movie> effectively.
//         */
//        int age = 18;
//        for(int i=0,j;i<=20;++i) for(j=0;j<2;++j) {
//            ResponseEntity<List<Movie>> response = restTemplate.exchange(
//                    "http://localhost:" + port + String.format("/recommend/user?gender=%c&age=%d&occ=%d", j==0?'F':'M', age, i),
//                    HttpMethod.GET,
//                    null,
//                    new ParameterizedTypeReference<List<Movie>>() {}
//            );
//            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//            // Extract the List<Movie> from the ResponseEntity
//            List<Movie> movies = response.getBody();
//            assertThat(movies).isNotNull(); // Which is not the 'error' case, but it is far from the ideal case.
//            if(movieMin>movies.size()) movieMin = movies.size();
//            logger.info(String.format("movie size for age: %d, gender: %c, with occ: %d is %d", age, j==0?'F':'M', i, movies.size()));
//        }
//        logger.info(String.format("movieMin for age: %d is %d", age, movieMin));
//    }

    // Takes about 7'.
//    @ParameterizedTest
//    @DisplayName("Testing for all valid inputs")
//    @ValueSource(ints = {1, 18, 25, 35, 45, 50, 56})
//    public void testRecByUserAll(int age) throws Exception {
//        int movieMin = Integer.MAX_VALUE;
//        /*
//        exchange method is used instead of getForEntity for better control over the request and to specify the type reference explicitly.
//        HttpMethod.GET is specified as the HTTP method.
//        new ParameterizedTypeReference<List<Movie>>() {} maintains generic type information during runtime, enabling the RestTemplate to convert the incoming JSON into a List<Movie> effectively.
//         */
//        for(int i=0,j;i<=20;++i) for(j=0;j<2;++j) {
//            ResponseEntity<List<Movie>> response = restTemplate.exchange(
//                    "http://localhost:" + port + String.format("/recommend/user?gender=%c&age=%d&occ=%d", j==0?'F':'M', age, i),
//                    HttpMethod.GET,
//                    null,
//                    new ParameterizedTypeReference<List<Movie>>() {}
//            );
//            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//            // Extract the List<Movie> from the ResponseEntity
//            List<Movie> movies = response.getBody();
//            assertThat(movies).isNotNull(); // Which is not the 'error' case, but it is far from the ideal case.
//            if(movieMin>movies.size()) movieMin = movies.size();
//        }
//        logger.info(String.format("movieMin for age: %d is %d", age, movieMin));
//    }
}
