package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.example.App.movieinfo.model.User.genderChecker;
import static com.example.App.movieinfo.model.User.occChecker;

@RestController
@RequestMapping("/recommend")
public class RecController {

    // curl "http://localhost:8080/recommend/user?gender=F&age=18&occ=20"
    @GetMapping(value = "user", params = {"gender", "age", "occ"})
    public List<Movie> recByUser(
            @RequestParam("gender") String gender,
            @RequestParam(value = "age", required = true) Long age,
            @RequestParam("occ") Long occ) {
        if(!genderChecker(gender) || !occChecker(occ)) {
            throw new IllegalStateException(String.format("gender: %s (\"F\" or \"M\") or occ: %d (0-20) does not match within the normal condition.", gender, occ));
        }
        return new ArrayList<>();
    }
}
