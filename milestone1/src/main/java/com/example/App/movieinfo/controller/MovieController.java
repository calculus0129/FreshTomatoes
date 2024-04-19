package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.model.Movie;
import com.example.App.movieinfo.model.Rating;
import com.example.App.movieinfo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    MongoTemplate template;

    private final MovieRepository movieRepository;

    @Autowired
    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

//    @GetMapping("/averageRating/{rating}")
//    public ResponseEntity<List<MovieAverageRating>> getMoviesByAverageRating(@PathVariable double rating) {
//        List<MovieAverageRating> movies = movieService.findMoviesWithAverageRatingAbove(rating);
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(movies);
//    }

//    @GetMapping("/{id}")
//    public Movie getMovieById(@PathVariable String id) {
//        return movieRepository.findById(id).orElse(null);
//    }

    // get movies with ratings higher or equal to given rating.
    // Resolved [org.springframework.web.bind.MissingPathVariableException: Required URI template variable 'r' for method parameter type Long is not present]
    @GetMapping("/averageRating/{r}")
    public ResponseEntity<?> getMoviesByAverageRating(@PathVariable Long r) {
        if(r < 1L || r > 5L) return ResponseEntity.badRequest().body("You should input integer rating from 1 to 5.");

        List<Long> movieIds = template.query(Rating.class).distinct("movieId").as(Long.class).all();
        List<Long> resMovieIds = movieIds.stream()
                .filter(a -> template.query(Rating.class).matching(query(where("movieId").is(a))).all()
                        .stream()
                        .mapToLong(Rating::getRating)
                        .average().orElse(0) >= r)
                .collect(Collectors.toList());
        if (resMovieIds.isEmpty()) return ResponseEntity.ok("There are no such movies."); // throw new MovieNotFoundException(r);
        List<Movie> movies = template.query(Movie.class).matching(query(where("movieId").in(resMovieIds))).all();
        if (movies.isEmpty()) return ResponseEntity.ok("There are no such movies."); // throw new MovieNotFoundException(r);

        return ResponseEntity.ok().body(movies);
    }

    @GetMapping("/{movieID}")
    public ResponseEntity<Movie> getMovieByMovieId(@PathVariable Long movieID) {
        return movieRepository.findByMovieId(movieID)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // The return type allows us to return both the status code and the body depending on the situation.
    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody Movie movie) {
        if (movieRepository.findByMovieId(movie.getMovieId()).isPresent()) {
            // Return 409 Conflict when the movie already exists
            String errorMessage = "A movie with such a movieID already exists in the movie repository!";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
        Movie savedMovie = movieRepository.save(movie);
        // Return 201 Created and the saved movie
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @PutMapping("/{movieID}")
    public ResponseEntity<?> updateMovie(@PathVariable Long movieID, @RequestBody Movie updatedMovie) {
        if(!Objects.equals(movieID, updatedMovie.getMovieId())) {
            String errorMessage = String.format("The provided movieId: %d is not equal to the movieId: %d of the movie information!", movieID, updatedMovie.getMovieId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
        return movieRepository.findByMovieId(movieID)
                .map(movie -> {
                    movie.setTitle(updatedMovie.getTitle());
                    movie.setGenres(updatedMovie.getGenres());
                    Movie savedMovie = movieRepository.save(movie);
                    return ResponseEntity.ok(savedMovie);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{movieId}")
    ResponseEntity<?> deleteMovie(@PathVariable Long movieId) {
        movieRepository.deleteByMovieId(movieId);
        return ResponseEntity.noContent().build();
    }
}
