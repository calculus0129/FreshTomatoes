package com.example.App.movieinfo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.App.movieinfo.model.Movie;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends MongoRepository<Movie, Long> {
}