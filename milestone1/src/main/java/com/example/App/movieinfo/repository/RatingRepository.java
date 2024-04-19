package com.example.App.movieinfo.repository;


import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.WriteModel;
import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.App.movieinfo.model.Rating;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.Query;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {
    List<Rating> findByMovieId(Long movieId);
    // Spring Data will handle the Pageable parameter and generate the necessary query to fetch only the specified number of results.
    List<Rating> findByMovieId(Long movieId, Pageable pageable);

    // Add a method to fetch ratings with a limit
//    @Query(value = "{ 'movieId' : ?0 }")
//    List<Rating> findByMovieIdWithLimit(Long movieId, Pageable pageable);
    Optional<Rating> findByUserIdAndMovieId(Long userId, Long movieId);
}

/*
Using Pageable:
This change uses Spring Data's Pageable to add pagination to the database query,
which effectively limits the number of results based on the max parameter.
The PageRequest.of(0, max) creates a Pageable object that requests the first max items.
Conditional Query Execution:
If max is provided and is greater than zero,
the method findByMovieIdWithLimit is called.
Otherwise, it defaults to fetching all ratings for the movie ID without pagination.

 */