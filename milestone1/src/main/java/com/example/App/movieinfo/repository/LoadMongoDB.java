package com.example.App.movieinfo.repository;

import com.example.App.movieinfo.model.Movie;
import com.example.App.movieinfo.model.Rating;
import com.example.App.movieinfo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class LoadMongoDB implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(LoadMongoDB.class);

    @Autowired
    public LoadMongoDB(MovieRepository movieRepository, RatingRepository ratingRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        clearData();
        log.info("Preloading Movie data...");
        loadMovies();
        log.info("Movie data all uploaded!");
        log.info("Preloading rating data...");
        loadRatings();
        log.info("Rating data all uploaded!");
        log.info("Preloading User data...");
        loadUsers();
        log.info("User data all uploaded!");
    }

    private void clearData() {
        log.info("Clearing previous data from collections...");
        movieRepository.deleteAll();
        ratingRepository.deleteAll();
        userRepository.deleteAll();
        log.info("Cleared!");
    }

    private void loadMovies() throws Exception {
        log.info("Root Path: {}", getClass().getClassLoader().getResource("").getPath());
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/movies.dat");
        if (inputStream == null) {
            log.error("Cannot find 'movies.dat' in classpath");
//            throw new IllegalStateException("Cannot find 'movies.dat' in classpath");
        }

        try (var br = new BufferedReader(new InputStreamReader(inputStream))) {
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
//            throw e;
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

    private void loadUsers() throws Exception {
        Path filePath = Paths.get("data/users.dat");
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            List<User> users = lines.map(line -> line.split("::"))
                    .filter(parts -> parts.length >= 5)
                    .map(parts -> new User(Long.parseLong(parts[0]), parts[1], Long.parseLong(parts[2]), Long.parseLong(parts[3])))
                    .collect(Collectors.toList());
            userRepository.saveAll(users);
        } catch (Exception e) {
            log.error("Error preloading users: {}", e.getMessage());
        }
    }
}