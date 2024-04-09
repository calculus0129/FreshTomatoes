package com.example.App.movieinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "movie")
public class Movie { // Spring data mongodb maps the Movie class into a collection called "movie"

    @JsonIgnore
    private @Id Long movieId; // mostly for internal use by db
    private String title;
    private String genre;

    public Movie() {}
    public Movie(Long movieId, String title, String genre) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
    }
    public Long getMovieId() {
        return this.movieId;
    }
    public String getTitle() {
        return this.title;
    }
    public String getGenre() {
        return this.genre;
    }
    
    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return String.format(
                "Movie[id=%s, title=%s, genre=%s]",
                movieId, title, genre);
    }
}