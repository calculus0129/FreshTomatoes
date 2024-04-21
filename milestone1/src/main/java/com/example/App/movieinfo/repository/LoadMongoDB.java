package com.example.App.movieinfo.repository;

import com.example.App.movieinfo.model.Movie;
import com.example.App.movieinfo.model.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
//import java.util.ArrayList;
import java.util.HashSet;
//import java.util.Optional;
import java.util.List;
import java.util.Set;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class LoadMongoDB implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;
    private static final Logger log = LoggerFactory.getLogger(LoadMongoDB.class);

    @Autowired
    public LoadMongoDB(MovieRepository movieRepository, RatingRepository ratingRepository) {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
    }
//    @Bean
//    CommandLineRunner initMongoDB(MovieRepository movieRepository, RatingRepository ratingRepository) throws FileNotFoundException {
//        return args -> {
//            BufferedReader movieReader = new BufferedReader(new FileReader("data/movies.dat"));
//            String temp;
//            log.info("Preloading Movie data...");
//            while ((temp = movieReader.readLine()) != null) {
//                String[] movie = temp.trim().split("::");
//                if(movie.length>=3) {
//                    movieRepository.save(new Movie(Long.parseLong(movie[0]), movie[1], new HashSet<>(Set.of(movie[2].split("\\|")))));
//                }
//            }
//            log.info("Movie data all uploaded!");
//            movieReader.close();
//
//            BufferedReader ratingReader = new BufferedReader(new FileReader("data/ratings.dat"));
//            log.info("Preloading rating data...");
//            while ((temp = ratingReader.readLine()) != null) {
//                String[] rating = temp.trim().split("::");
//                if(rating.length>=4) {
//                    ratingRepository.save(new Rating(Long.parseLong(rating[0]), Long.parseLong(rating[1]), Long.parseLong(rating[2]), Long.parseLong(rating[3])));
//                }
//            }
//            log.info("Rating data all uploaded!");
//            ratingReader.close();
//        };
//    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Preloading Movie data...");
        loadMovies();
        log.info("Movie data all uploaded!");
        log.info("Preloading rating data...");
        loadRatings();
        log.info("Rating data all uploaded!");
    }


    private void loadMovies() throws Exception {
        try (var br = new BufferedReader(new FileReader("data/movies.dat"))) {
            String line;
            while ((line = br.readLine()) != null) {
                var parts = line.trim().split("::");
                if (parts.length >= 3) {
                    long movieID = Long.parseLong(parts[0]);
                    var title = parts[1];
                    var genres = parts[2].split("\\|");
                    Set<String> genreSet = new HashSet<>(Set.of(genres));
                    Movie movie = new Movie(movieID, title, genreSet);
                    movieRepository.save(movie);
                }
            }
        } catch (Exception e) {
            log.error("Error preloading movies: {}", e.getMessage());
        }
    }

    private void loadRatings() throws Exception {
//        long userID = Long.parseLong(parts[0]);
//        long movieID = Long.parseLong(parts[1]);
//        long rating = Long.parseLong(parts[2]);
//        long timestamp = Long.parseLong(parts[3]);
//        Rating ratingObj = new Rating(userID, movieID, rating, timestamp);
        Path filePath = Paths.get("data/ratings.dat");
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            List<Rating> ratings = lines.parallel()
                    .map(line -> line.split("::"))
                    .filter(parts -> parts.length >= 4)
                    .map(parts -> new Rating(Long.parseLong(parts[0]), Long.parseLong(parts[1]), Long.parseLong(parts[2]), Long.parseLong(parts[3])))
                    .collect(Collectors.toList());

            ratingRepository.saveAll(ratings);
        } catch (Exception e) {
            log.error("Error preloading ratings: {}", e.getMessage());
        }
    }

}