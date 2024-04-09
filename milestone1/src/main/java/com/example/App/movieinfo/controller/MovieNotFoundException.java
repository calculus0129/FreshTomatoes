package com.example.App.movieinfo.controller;

public class MovieNotFoundException extends RuntimeException {

    MovieNotFoundException(String id) {
        super("Could not find Movie with ID " + id);
    }
    MovieNotFoundException() {super("Rating scale is from 1 to 5");}
    MovieNotFoundException(Long r) {super("There's no movie with rating higher than or equal to " + r);}
}