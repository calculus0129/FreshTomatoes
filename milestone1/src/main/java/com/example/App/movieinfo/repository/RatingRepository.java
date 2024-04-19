package com.example.App.movieinfo.repository;


import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.WriteModel;
import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.App.movieinfo.model.Rating;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends MongoRepository<Rating, Long> {

}