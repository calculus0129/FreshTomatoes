package com.example.App.movieinfo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.App.movieinfo.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByUserId(Long userId); // This method allows you to fetch a movie by its movieID.
    //void deleteByMovieId(Long movieId);
}