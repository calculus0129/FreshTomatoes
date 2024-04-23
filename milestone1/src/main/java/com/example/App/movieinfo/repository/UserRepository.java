package com.example.App.movieinfo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.App.movieinfo.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserId(Long userId); // This method allows you to fetch a movie by its movieID.
    List<User> findByGenderAndAgeAndOccupation(String gender, Long age, Long occupation);
    //void deleteByMovieId(Long movieId);
}