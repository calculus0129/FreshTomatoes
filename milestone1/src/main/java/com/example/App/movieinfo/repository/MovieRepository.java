package com.example.App.movieinfo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.App.movieinfo.model.Movie;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    Optional<Movie> findByMovieId(Long movieId); // This method allows you to fetch a movie by its movieID.
    void deleteByMovieId(Long movieId);
}