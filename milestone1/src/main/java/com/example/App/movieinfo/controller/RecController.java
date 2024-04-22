package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.model.Movie;
import com.example.App.movieinfo.model.Rating;
import com.example.App.movieinfo.model.User;
import com.example.App.movieinfo.repository.MovieRepository;
import com.example.App.movieinfo.repository.RatingRepository;
import com.example.App.movieinfo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.App.movieinfo.model.User.*;
import static com.example.App.movieinfo.util.Recommendation.recByRatings;

@RestController
@RequestMapping("/recommend")
public class RecController {

    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecController(MovieRepository movieRepository, RatingRepository ratingRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    // curl "http://localhost:8080/recommend/user?gender=F&age=18&occ=20"
    // org.springframework.dao.IncorrectResultSizeDataAccessException: Query { "$java" : Query: { "movieId" : 1}, Fields: {}, Sort: {} } returned non unique result
    //
    @GetMapping(value = "user", params = {"gender", "age", "occ"})
    public ResponseEntity<?> recByUser(
            @RequestParam("gender") String gender,
            @RequestParam(value = "age", required = true) Long age,
            @RequestParam("occ") Long occ) {
        // Check if the gender and occupations are valid.
        if(!genderChecker(gender) || !occChecker(occ)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("gender: %s (\"F\" or \"M\") or occ: %d (0-20) does not match within the normal condition.", gender, occ));
            //IllegalStateException();
        }
        age = ageMapper(age);

        Set<Long> userIds = userRepository.findByGenderAndAgeAndOccupation(gender, age, occ).stream().map(User::getUserId).collect(Collectors.toCollection(TreeSet::new));
        List<Rating> ratingList = userIds.stream().parallel().map(uid -> ratingRepository.findByUserId(uid)).reduce(new ArrayList<>(), (res, elem) -> {
            return Stream.concat(res.stream(), elem.stream()).collect(Collectors.toList());
        });

        return ResponseEntity.ok(recByRatings(movieRepository, ratingList));
    }
}
