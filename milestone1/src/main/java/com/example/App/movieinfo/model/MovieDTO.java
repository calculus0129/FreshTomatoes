package com.example.App.movieinfo.model;

public class MovieDTO {
    // title of the movie
    private String title;
    private String genre;
    // number of ratings for the movie
    private int count;
    public MovieDTO(String title, String genre, int count) {
        this.title = title;
        this.genre = genre;
        this.count = count;
    }
    public String getTitle() {return title;}
    public String getGenre() {return genre;}
    public int getCount() {return count;}
}
