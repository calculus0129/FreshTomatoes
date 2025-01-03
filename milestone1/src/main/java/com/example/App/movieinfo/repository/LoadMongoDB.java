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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/ratings.dat");
        if (inputStream == null) {
            log.error("Cannot find 'ratings.dat' in classpath");
            throw new IllegalStateException("Cannot find 'ratings.dat' in classpath");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<Rating> ratings = reader.lines()
                    .parallel()
                    .map(line -> line.split("::"))
                    .filter(parts -> parts.length >= 4)
                    .map(parts -> new Rating(Long.parseLong(parts[0]), Long.parseLong(parts[1]), Long.parseLong(parts[2]), Long.parseLong(parts[3])))
                    .collect(Collectors.toList());

            ratingRepository.saveAll(ratings);
        } catch (Exception e) {
            log.error("Error preloading ratings: {}", e.getMessage());
            throw e; // Rethrow to ensure the caller is aware of the failure
        }
    }

    private void loadUsers() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/users.dat");
        if (inputStream == null) {
            log.error("Cannot find 'users.dat' in classpath");
            throw new IllegalStateException("Cannot find 'users.dat' in classpath");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<User> users = reader.lines()
                    .map(line -> line.split("::"))
                    .filter(parts -> parts.length >= 5)
                    .map(parts -> new User(Long.parseLong(parts[0]), parts[1], Long.parseLong(parts[2]), Long.parseLong(parts[3])))
                    .collect(Collectors.toList());
            userRepository.saveAll(users);
        } catch (Exception e) {
            log.error("Error preloading users: {}", e.getMessage());
            throw e; // Rethrow to ensure that the error is not silently ignored
        }
    }
}