package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.model.Movie;
import com.example.App.movieinfo.model.MovieDTO;
import com.example.App.movieinfo.model.Rating;
import com.example.App.movieinfo.repository.CsvFileGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
@RequestMapping("/movieInfo")
public class VisController {
    private static final Logger log = LoggerFactory.getLogger(VisController.class);

    @Autowired
    MongoTemplate template;

    @Autowired
    CsvFileGenerator csvGenerator;
    // list of movie genres
    String[] movieGenreList = {"Action", "Adventure", "Animation", "Children's", "Comedy", "Crime", "Documentary",
            "Drama", "Fantasy", "Film-Noir", "Horror", "Musical", "Mystery", "Romance", "Sci-Fi",
            "Thriller", "War", "Western"};
    List<String> movieGenres = Arrays.asList(movieGenreList);
    // state variable for reuse of the Popularity timeline data
    private boolean Initialized = false;

    // timeline data
    private HashMap<String, Double[]> genreTimeline;
    private HashMap<Integer, List<MovieDTO>> popularMovies;

    private int start = 0;
    private int end = 0;
    private int[] time;
    @GetMapping("/csv/timeline")
    public void exportIntoCSVtimelines(HttpServletResponse response) throws IOException {
        if (!(this.Initialized)) initialize_movieInfo_data();
        response.setContentType("text/csv");
        csvGenerator.writeTimelinesToCsv(genreTimeline, time, response.getWriter());
    }
    @GetMapping("/csv/timeline/one")
    public void exportIntoCSVtimeline (HttpServletResponse response, @RequestParam String g) throws IOException {
        if (!movieGenres.contains(g)) throw new MovieNotFoundException();
        if (!(this.Initialized)) initialize_movieInfo_data();
        response.setContentType("text/csv");
        csvGenerator.writeTimelineToCsv(genreTimeline.get(g), time, response.getWriter());
    }
    @GetMapping("/csv/year")
    public void exportIntoCSVyear (HttpServletResponse response, @RequestParam(value = "y", required = false) Integer y, @RequestParam(value = "t", required = false) String t) throws IOException{
        if (y == null && t == null) throw new MovieNotFoundException();
        if (!(this.Initialized)) initialize_movieInfo_data();
        if (y != null) {
            if (y > end || y < start) throw new MovieNotFoundException();
            response.setContentType("text/csv");
            csvGenerator.writeMoviesToCsv(popularMovies.get(y), response.getWriter());
        } else {
            List<String> movieTitles = template.query(Movie.class).distinct("title").as(String.class).all();
            if (!movieTitles.contains(t)) throw new MovieNotFoundException();
            csvGenerator.writeMoviesToCsv(popularMovies.get(Movie.getYear(t)), response.getWriter());
        }
    }
    @GetMapping("/genres")
    public ResponseEntity<List<String>> autoMovieGenre(@RequestParam String s) {
        /*return autocompleted recommendation string of movie genre*/
        List<String> temp = movieGenres.stream().filter(genre -> strMatch(s, genre)).toList();
        if (temp.size() > 4) {
            return ResponseEntity.ok().body(temp.subList(0, 4));
        } else return ResponseEntity.ok().body(temp);
    }
    @GetMapping("/titles")
    public ResponseEntity<List<String>> autoMovieTitle(@RequestParam String s) {
        /*return autocompleted recommendation string of movie name*/
        List<String> movieTitles = template.query(Movie.class).distinct("title").as(String.class).all();
        List<String> temp = movieTitles.stream().filter(title -> strMatch(s, title)).toList();
        if (temp.size() > 4) {
            return ResponseEntity.ok().body(temp.subList(0, 4));
        } else return ResponseEntity.ok().body(temp);
    }
    public void initialize_movieInfo_data() {
        // get movies that rating have been done
        List<Long> movieIds = template.query(Rating.class).distinct("movieId").as(Long.class).all();
        List<Movie> movies = template.query(Movie.class).matching(query(where("movieId").in(movieIds))).all();
        /*
        timeline range
         */
        start = movies.get(0).getYear();
        end = start;
        movies.forEach(movie -> {
            int temp = movie.getYear();
            if (temp < start) {
                start = temp;
            } else if (temp > end) {
                end = temp;
            }
        });
        int num = end - start + 1;
        /*
        build "popularity timeline1" based on movie number "for each genre"
         */
        HashMap<String, Double[]> genreTimeline = new HashMap<>();
        // 1- initialize popularity timeline of each genre
        movieGenres.forEach(genre -> {
            Double[] timeline = new Double[num];
            Arrays.fill(timeline, 0.0);
            genreTimeline.put(genre, timeline);
        });
        // 2- update popularity by using each movie's popularity
        movies.forEach(movie -> {
            int genre_num = movie.getGenres().size();
            movie.getGenres().forEach(genre -> {
                Double[] temp = genreTimeline.get(genre);
                int ind = movie.getYear()-start;
                temp[ind] += (double) 1 / genre_num;
                // each movie's popularity is distributed to the genre's popularity it is included in (division: one decimal point)
            });
        });
        /*
        lookup for each movie's rating count
         */
        HashMap<Long, Integer> counts = new HashMap<>();
        movieIds.forEach(id -> counts.put(id, 0));
        template.query(Rating.class).all().forEach(rating -> {
            Long id = rating.getMovieId();
            counts.put(id, counts.get(id) + 1);
        });
        /*
        build "popular movie list" for each year
         */
        HashMap<Integer, List<MovieDTO>> popularMovies = new HashMap<>();
        for (int year = start; year <= end; year++) {
            popularMovies.put(year, new LinkedList<>());
        }
        movies.sort(Comparator.comparingInt(movie -> counts.get(movie.getMovieId())));
        movies.forEach(movie -> {
            popularMovies.get(movie.getYear()).add(new MovieDTO(movie.getTitle(), movie.getGenres().toString(), counts.get(movie.getMovieId())));
        });
        // initialize data
        this.genreTimeline = genreTimeline;
        this.popularMovies = popularMovies;
        this.Initialized = true;
        int[] temp = new int[num];
        for (int i = 0; i < num; i++) {
            temp[i] = start+i;
        }
        this.time = temp;
    }

    public boolean strMatch(String str1, String match) {
        if (str1.length() > match.length()) return false;
        else {
            for (int i = 0; i < str1.length(); i++) {
                if (str1.charAt(i) != match.charAt(i)) return false;
            }
            return true;
        }
    }
}
