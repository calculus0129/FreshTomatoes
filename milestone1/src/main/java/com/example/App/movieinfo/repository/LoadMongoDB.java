package com.example.App.movieinfo.repository;

import com.example.App.movieinfo.model.Movie;
import com.example.App.movieinfo.model.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Configuration
public class LoadMongoDB {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    @Bean
    CommandLineRunner initMongoDB(MovieRepository movieRepository, RatingRepository ratingRepository) throws FileNotFoundException {
        BufferedReader movieReader = new BufferedReader(new FileReader("data/movies.dat"));
        BufferedReader ratingReader = new BufferedReader(new FileReader("data/ratings.dat"));
        return args -> {
            String temp;
            while ((temp = movieReader.readLine()) != null) {
                String[]movie = temp.split("::");
                log.info("Preloading Movie data " + movieRepository.save(
                        new Movie(Long.parseLong(movie[0]), movie[1], movie[2].replace("\n", ""))));
            }
            log.info("Movie data all uploaded");
            while ((temp = ratingReader.readLine()) != null) {
                String[]rating = temp.split("::");
                log.info("Preloading rating data " + ratingRepository.save(
                        new Rating(Long.parseLong(rating[0]), Long.parseLong(rating[1]), Long.parseLong(rating[2]), Long.parseLong(rating[3].replace("\n", "")))));
            }
            log.info("Rating data all uploaded");
            movieReader.close();
            ratingReader.close();
        };
    }
}
