package com.example.App.movieinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.Set;


@Document(collection = "movie")
public class Movie { // Spring data mongodb maps the Movie class into a collection called "movie"
    @Id
    private String id; // MongoDB's default ID type is String. This Id is for auto-generation. So it should not be a Long type.
    private Long movieId; // mostly for internal use by db
    private String title;
    private Set<String> genres;

    public Movie() {}
    public Movie(Long movieId, String title, Set<String> genres) {
        this.movieId = movieId;
        this.title = title;
        this.genres = genres;
    }

    public String getId() {
        return this.id;
    }
    public Long getMovieId() {
        return this.movieId;
    }
    public String getTitle() {
        return this.title;
    }
    public Set<String> getGenres() {
        return this.genres;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id)
                && Objects.equals(movieId, movie.movieId)
                && Objects.equals(title, movie.title)
                && Objects.equals(genres, movie.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movieId, title, genres);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", movieID=" + movieId +
                ", title='" + title + '\'' +
                ", genres=" + genres +
                '}';
    }
}