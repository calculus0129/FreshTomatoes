package com.example.App;

import com.example.App.movieinfo.repository.MovieRepository;
import com.example.App.movieinfo.repository.RatingRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {MovieRepository.class, RatingRepository.class})
public class WebApp {

    public static void main(String... args) {
        SpringApplication.run(WebApp.class, args);
    }
}