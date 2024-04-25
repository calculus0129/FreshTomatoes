package com.example.App.movieinfo.controller;

public class MovieNotFoundException extends RuntimeException {

    MovieNotFoundException() {super("Movie is not present");}
    MovieNotFoundException(Long r) {super("There's no movie with rating higher than or equal to " + r);}
}