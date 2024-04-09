package com.example.App.movieinfo.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.App.movieinfo.model.Rating;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends MongoRepository<Rating, Long> {
}