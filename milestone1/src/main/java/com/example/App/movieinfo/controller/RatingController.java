package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.model.Movie;
import com.example.App.movieinfo.model.Rating;
import com.example.App.movieinfo.model.User;
import com.example.App.movieinfo.repository.MovieRepository;
import com.example.App.movieinfo.repository.RatingRepository;
import com.example.App.movieinfo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public RatingController(RatingRepository ratingRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    // Get all ratings with optional max parameter
    // curl http://localhost:8080/ratings?max=10
    // calling curl http://localhost:8080/ratings may result in command line buffer issue with lags.
    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings(@RequestParam(value = "max", required = false) Integer max) {
        List<Rating> allRatings = ratingRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(max != null ? allRatings.stream().limit(max).collect(Collectors.toList()) : allRatings);
    }

    // Get ratings for a specific movie using a query parameter with optional max parameter
    // curl "http://localhost:8080/ratings?movieId=1&max=5"
    @GetMapping(params = "movieId")
    public ResponseEntity<List<Rating>> getRatingsByMovieId(@RequestParam("movieId") Long movieId,
                                            @RequestParam(value = "max", required = false) Integer max) {
        // If max is not null and greater than 0, apply the PageRequest with max as the page size
        if (max != null && max > 0) {
            Pageable pageable = PageRequest.of(0, max);
            return ResponseEntity.ok(ratingRepository.findByMovieId(movieId, pageable));
        } else {
            // Else, return all ratings without pagination
            return ResponseEntity.ok(ratingRepository.findByMovieId(movieId));
        }
    }

    // Get a specific rating by userId and movieId
    // curl "http://localhost:8080/ratings?userId=6&movieId=1"
    @GetMapping(params = {"userId", "movieId"})
    public ResponseEntity<?> getRatingByUserIdAndMovieId(@RequestParam("userId") Long userId,
                                                         @RequestParam("movieId") Long movieId) {
        Optional<Rating> rating = ratingRepository.findByUserIdAndMovieId(userId, movieId);

        return rating
                .<ResponseEntity<?>>map(ResponseEntity::ok) // Explicitly defining the generic type
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rating not found for userId " + userId + " and movieId " + movieId));
    }

    // PageRequest.of(0, max) creates a Pageable object that requests a page of data with the size of max. The first argument 0 signifies that we want the first page.
    // ratingRepository.findByMovieId(movieId, pageable).getContent() will only fetch the number of records up to max. The getContent() method is used to extract the list of entities from the Page object.

    // Update a rating by userId and movieId
    // curl -X PUT "http://localhost:8080/ratings?userId=6&movieId=1" -H "Content-Type: application/json" -d '{"rating": 3, "timestamp": 999999999}'
    @PutMapping
    public ResponseEntity<?> updateRating(@RequestParam("userId") Long userId,
                                          @RequestParam("movieId") Long movieId,
                                          @RequestBody Rating updatedRating) {
        // Check if the user and movie are present.
        Optional<Movie> movieOptional = movieRepository.findByMovieId(movieId);
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if(movieOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found for movieId " + movieId);
        }
        if(userOptional.isEmpty()) {
            // No rating found, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for userId " + userId);
        }

        // Check if the existing rating is present
        Optional<Rating> existingRatingOptional = ratingRepository.findByUserIdAndMovieId(userId, movieId);
        if (!existingRatingOptional.isPresent()) {
            // No rating found, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rating not found for userId " + userId + " and movieId " + movieId);
        }

        // Update the found rating
        Rating existingRating = existingRatingOptional.get();
        existingRating.setRating(updatedRating.getRating());
        existingRating.setTimeStamp(updatedRating.getTimestamp());

        // Save the updated rating
        Rating savedRating = ratingRepository.save(existingRating);

        // Return 200 OK with the updated rating data
        return ResponseEntity.ok(savedRating);
    }


    // The return type allows us to return both the status code and the body depending on the situation.
    // curl -X POST "http://localhost:8080/ratings" -H "Content-Type: application/json" -d '{"userId":7000, "movieId":1, "rating": 5, "timestamp": 922222222}'
    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody Rating rating) {
        if (ratingRepository.findByUserIdAndMovieId(rating.getUserId(), rating.getMovieId()).isPresent()) {
            // Return 409 Conflict when the movie already exists
            String errorMessage = "A rating with same (userId, movieId) already exists in the rating repository!";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
        Rating savedRating = ratingRepository.save(rating);
        // Return 201 Created and the saved movie
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRating);
    }


    // Delete a rating by userId and movieId
    // curl -X DELETE "http://localhost:8080/ratings?userId=7000&movieId=1"
    @DeleteMapping
    public ResponseEntity<?> deleteRating(@RequestParam("userId") Long userId, @RequestParam("movieId") Long movieId) {
        // Attempt to find the rating based on userId and movieId
        Optional<Rating> ratingOptional = ratingRepository.findByUserIdAndMovieId(userId, movieId);
        if (!ratingOptional.isPresent()) {
            // No rating found, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rating not found for userId " + userId + " and movieId " + movieId);
        }

        // If found, delete the rating
        ratingRepository.delete(ratingOptional.get());

        // Return 200 OK if deletion is successful
        return ResponseEntity.ok().body("Rating successfully deleted for userId " + userId + " and movieId " + movieId);
    }

}
