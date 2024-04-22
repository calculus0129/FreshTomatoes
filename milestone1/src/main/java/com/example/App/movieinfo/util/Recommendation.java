package com.example.App.movieinfo.util;
import com.example.App.movieinfo.model.*;
import com.example.App.movieinfo.repository.MovieRepository;

import java.util.*;
import java.util.stream.Collectors;

//@Component
public class Recommendation {
    public static List<Movie> recByRatings(MovieRepository movieRepository, Collection<Rating> ratingCollection) {
        // Assumption: There must be no invalid movieId in the ratings. Else, the '.get()' part will throw noSuchElement
        Map<Long, AbstractMap.SimpleEntry<Long, Integer>> midToRating = new TreeMap<>();
        for(Rating r : ratingCollection) {
            midToRating.merge(r.getMovieId(), new AbstractMap.SimpleEntry<>(r.getRating(), 1), (entry, value) -> new AbstractMap.SimpleEntry<>(
                    entry.getKey() + value.getKey(),
                    entry.getValue() + value.getValue()
            ));
        }
        return midToRating.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(
                        movieRepository.findByMovieId(entry.getKey()).orElseThrow(()-> new RuntimeException("Could not find Movie with ID " + entry.getKey()))
                        , entry.getValue()))
                .sorted((a, b) -> {
                    /* Sorting Criterion
                     * 1. Average Rating
                     * 2. Number of Review
                     * 3. movieTitle
                     */
                    long aRating = a.getValue().getKey()*b.getValue().getValue(), bRating = a.getValue().getValue() * b.getValue().getKey();
                    if(aRating == bRating) {
                        if(a.getValue().getValue().equals(b.getValue().getValue())) {
                            return a.getKey().getTitle().compareTo(b.getKey().getTitle());
                        } else {
                            return a.getValue().getValue().compareTo(b.getValue().getValue());
                        }
                    } else {
                        return (aRating < bRating) ? -1 : 1;
                    }
                })
                .map(AbstractMap.SimpleEntry::getKey)
                .collect(Collectors.toList());
    }
}

