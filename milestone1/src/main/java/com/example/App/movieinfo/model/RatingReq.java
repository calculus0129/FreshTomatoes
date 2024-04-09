package com.example.App.movieinfo.model;

public class RatingReq extends Rating{
    private final String movieTitle;
    RatingReq(Long userId, String movieTitle, Long rating) {
        super(userId, Long.valueOf("0"), rating);
        this.movieTitle = movieTitle;
    }
    public String getMovieTitle() {return this.movieTitle;}
}
