package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.repository.MovieRepository;
import com.example.App.movieinfo.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class VisController {
    String[] movieGenres = {"Action", "Adventure", "Animation", "Children's", "Comedy", "Crime", "Documentary",
                            "Drama", "Fantasy", "Film-Noir", "Horror", "Musical", "Mystery", "Romance", "Sci-Fi",
                            "Thriller", "War", "Western"};

    @Autowired
    MongoTemplate template;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    RatingRepository ratingRepository;

    //@GetMapping("/csvData")




    @GetMapping("/movieInfo/genres")
    public List<String> autoMovieGenre(@RequestParam String s) {
        /*return autocompleted string of movie genre*/
        List<String> movieGenres = new LinkedList<>();

    }

    @GetMapping("/movieInfo/names")
    public List<String> autoMovieName(@RequestParam String s) {

    }
}
