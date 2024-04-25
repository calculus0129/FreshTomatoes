package com.example.App.movieinfo.controller;

public class MovieNotFoundException extends RuntimeException {

    MovieNotFoundException() {super("Movie is not present");}
}