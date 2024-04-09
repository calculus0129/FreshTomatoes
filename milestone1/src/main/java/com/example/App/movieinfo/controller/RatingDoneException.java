package com.example.App.movieinfo.controller;

public class RatingDoneException extends RuntimeException{
    RatingDoneException(String title) {super("Rating is already done with the movie " + title);}
}
