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
import java.util.HashSet;
import java.util.Set;

@Configuration
public class LoadMongoDB {
    private static final Logger log = LoggerFactory.getLogger(LoadMongoDB.class);
    @Bean
    CommandLineRunner initMongoDB(MovieRepository movieRepository, RatingRepository ratingRepository) throws FileNotFoundException {
        return args -> {
            BufferedReader movieReader = new BufferedReader(new FileReader("data/movies.dat"));
            String temp;
            log.info("Preloading Movie data...");
            while ((temp = movieReader.readLine()) != null) {
                String[] movie = temp.trim().split("::");
                if(movie.length>=3) {
                    movieRepository.save(new Movie(Long.parseLong(movie[0]), movie[1], new HashSet<>(Set.of(movie[2].split("\\|")))));
                }
            }
            log.info("Movie data all uploaded!");
            movieReader.close();

            BufferedReader ratingReader = new BufferedReader(new FileReader("data/ratings.dat"));
            log.info("Preloading rating data...");
            while ((temp = ratingReader.readLine()) != null) {
                String[] rating = temp.trim().split("::");
                if(rating.length>=4) {
                    ratingRepository.save(new Rating(Long.parseLong(rating[0]), Long.parseLong(rating[1]), Long.parseLong(rating[2]), Long.parseLong(rating[3])));
                }
            }
            log.info("Rating data all uploaded!");
            ratingReader.close();
        };
    }
}
