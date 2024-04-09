package com.example.App.movieinfo.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "rating")
public class Rating {

    private @MongoId Long userId;
    private Long movieId;
    private Long rating;
    private Long timestamp;

    public Rating(){}
    public Rating(Long userId, Long movieId, Long rating) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating =  rating;
        this.timestamp = System.currentTimeMillis() / 1000;
    }
    public Rating(Long userId, Long movieId, Long rating, Long timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return this.userId;
    }
    public Long getMovieId() {
        return this.movieId;
    }
    public Long getRating() {
        return this.rating;
    }
    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
    public void setRating(Long rating) {
        this.rating = rating;
    }
    public void setTimeStamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format(
                "Rating[userid=%s, movieid=%s, rating=%d, timestamp=%d]",
                userId, movieId, rating, timestamp);
    }
}