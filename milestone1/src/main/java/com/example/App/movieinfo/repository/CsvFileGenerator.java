package com.example.App.movieinfo.repository;

import com.example.App.movieinfo.model.MovieDTO;
import org.springframework.stereotype.Component;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

@Component
public class CsvFileGenerator {
    public void writeTimelinesToCsv(HashMap<String, Double[]> genreTimeline, int[] time, Writer writer) throws IOException {
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.print("genre");
        for (int t: time) {
            printer.print(t);
        }
        printer.println();
        for (Map.Entry<String, Double[]> entry: genreTimeline.entrySet()) {
            printer.print(entry.getKey());
            for (double d: entry.getValue()){
                printer.print(String.format("%.3f", d));
            }
            printer.println();
        }
    }
    public void writeTimelineToCsv(Double[] timeline, int[] time, Writer writer) throws IOException{
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printRecord("year", "count");
        for (int i = 0; i < time.length; i++) {
            printer.print(time[i]);
            printer.print(String.format("%.3f", timeline[i]));
            printer.println();
        }
    }
    public void writeMoviesToCsv(List<MovieDTO> movies, Writer writer) throws IOException{
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withDelimiter('\\'));
        printer.printRecord("title", "genres", "count");
        for (MovieDTO movie: movies) {
            printer.print(movie.getTitle());
            printer.print(movie.getGenre());
            printer.print(movie.getCount());
            printer.println();
        }
    }
}
