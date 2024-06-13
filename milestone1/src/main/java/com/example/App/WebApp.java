package com.example.App;

import com.example.App.movieinfo.repository.MovieRepository;
import com.example.App.movieinfo.repository.RatingRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


// Spring - auto load Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
@EnableMongoRepositories(basePackageClasses = {MovieRepository.class, RatingRepository.class})
public class WebApp extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WebApp.class);
    }
    public static void main(String... args) {
        SpringApplication.run(WebApp.class, args);
    }
}