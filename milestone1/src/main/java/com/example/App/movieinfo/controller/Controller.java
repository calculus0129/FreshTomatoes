package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.repository.MovieRepository;
import com.example.App.movieinfo.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.App.movieinfo.model.Rating;
import com.example.App.movieinfo.model.Movie;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
public class Controller {

    @Autowired
    MongoTemplate template;

    @Autowired
    MovieRepository movieRepository;
    @Autowired
    RatingRepository ratingRepository;


    @GetMapping("ratings")
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    // get movies with ratings higher or equal to given rating
//    @GetMapping("/ratings/{r}")
//    ResponseEntity<List<Movie>> MovieWithRating(@PathVariable Long r) {
//        if (r < 1 || r > 5) throw new MovieNotFoundException();
//
//        List<Long> movieIds = template.query(Rating.class).distinct("movieId").as(Long.class).all();
//        List<Long> resMovieIds = movieIds.stream()
//                .filter(a -> template.query(Rating.class).matching(query(where("movieId").is(a))).all()
//                        .stream()
//                        .mapToLong(Rating::getRating)
//                        .average().orElse(0) >= r)
//                .collect(Collectors.toList());
//        if (resMovieIds.isEmpty()) throw new MovieNotFoundException(r);
//        List<Movie> movies = template.query(Movie.class).matching(query(where("movieId").in(resMovieIds))).all();
//        if (movies.isEmpty()) throw new MovieNotFoundException(r);
//
//        return ResponseEntity.ok().body(movies);
//    }

    @GetMapping("movies")
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("movies/{movieID}")
    public ResponseEntity<Movie> getMovieByMovieID(@PathVariable Long movieID) {
        return movieRepository.findByMovieId(movieID)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @PostMapping("/users")
//    ResponseEntity<?> MovieNewRating(@RequestBody Rating rating) {
//        Long r = rating.getRating();
//        Long id = rating.getUserId();
//        Long mid = rating.getMovieId();
//
//        if (r < 1 || r > 5) throw new MovieNotFoundException();
//
//        List<Movie> m = template.query(Movie.class).matching(query(where("movieId").is(mid))).all();
//        if (m.isEmpty()) throw new MovieNotFoundException(mid.toString());
//        List<Rating> ra = template.query(Rating.class).matching(query(where("movieId").is(mid).and("userId").is(id))).all();
//        if (!ra.isEmpty()) throw new RatingDoneException(m.get(0).getTitle());
//
//        Rating newRating = new Rating(id, mid, r);
//
//
//        EntityModel<Rating> entityModel = EntityModel.of(ratingRepository.save(newRating),
//                linkTo(methodOn(Controller.class).MovieNewRating(rating)).withSelfRel());
//
//        return ResponseEntity //
//                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
//                .body(entityModel);
//    }
//
//    @PutMapping("/users/{x}")
//    ResponseEntity<?> replaceEmployee(@RequestBody Rating rating, @PathVariable Long x) {
//        Long r = rating.getRating();
//        Long id = x;
//        Long mid = rating.getMovieId();
//        if (r < 1 || r > 5) throw new MovieNotFoundException();
//
//        List<Movie> m = template.query(Movie.class).matching(query(where("movieId").is(mid))).all();
//        if (m.isEmpty()) throw new MovieNotFoundException(mid.toString());
//        List<Rating> ra = template.query(Rating.class).matching(query(where("movieId").is(mid).and("userId").is(id))).all();
//
//        Rating newRating = new Rating(id, mid, r);
//
//        EntityModel<Rating> entityModel = EntityModel.of(ratingRepository.save(newRating),
//                linkTo(methodOn(Controller.class).MovieNewRating(rating)).withSelfRel());
//
//        return ResponseEntity //
//                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
//                .body(entityModel);
//    }
}