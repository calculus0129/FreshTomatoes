package com.example.App.movieinfo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RatingDoneAdvice {
        @ResponseBody
        @ExceptionHandler(RatingDoneException.class)
        @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
        String RatingDoneHandler(RatingDoneException ex) {return ex.getMessage();}

}
