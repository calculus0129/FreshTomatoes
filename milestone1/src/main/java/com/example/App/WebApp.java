package com.example.App;

import com.example.App.movieinfo.repository.MovieRepository;
import com.example.App.movieinfo.repository.RatingRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


// Spring - auto load Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
@EnableMongoRepositories(basePackageClasses = {MovieRepository.class, RatingRepository.class})
public class WebApp {

    public static void main(String... args) {
        SpringApplication.run(WebApp.class, args);
    }
    // This method below is just for testing the jacoco.
    public static String suggestActivity(String day, int hour) {
        if (day.equals("Saturday") || day.equals("Sunday")) {
            if (hour >= 8 && hour <= 22) {
                return "Leisure activities";
            } else {
                return "Rest";
            }
        } else {  // Monday to Friday
            if (hour >= 9 && hour <= 17) {
                return "Business hours";
            } else {
                return "Off hours";
            }
        }
    }
}