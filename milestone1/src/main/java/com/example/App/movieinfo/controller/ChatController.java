package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.dto.ChatRequest;
import com.example.App.movieinfo.dto.ChatResponse;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;



@RestController
public class ChatController {
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    private String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    // GET REST API /chat
    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String plot, @RequestParam String genres, @RequestParam String stars, @RequestParam String directors) {
        String prompt_part1 =  "<Task Description>\nFrom the given partial infomation (plot, genres, stars, directors) about the movie that user is finding, the task is to extract meaningful infomation from the IMDb website. Please give me only one candidate.\n<Output Format>\nYou MUST respond in .json format without new lines. Do NOT include ```json and ``` at the first and last line. (refer to the example). Please respond in JSON format without any further explanation, allowing for straightforward refinement and integration into my web service.\n<In-context Example>\nLet’s take an example to understand how to extract meaningful infomation.\n\nQuestion: plot=they save the Earth, genres=action, stars=Chris Evans, directors=Anthony Russo\nAnswer:\n{\"title\": \"Avengers: Endgame\", \"imdb_link\": \"https://www.imdb.com/title/tt4154796/\", \"year\": 2019, \"parental_guide\": \"PG-13\", \"runtime\": \"181 min\", \"imdb_rating\": 8.4, \"genres\": [\"Action\", \"Adventure\", \"Drama\"], \"plot\": \"After the devastating events of Avengers: Infinity War (2018), the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.\", \"director\": [\"Anthony Russo\", \"Joe Russo\"], \"writers\": [\"Christopher Markus\", \"Stephen McFeely\"], \"stars\": [\"Robert Downey Jr.\", \"Chris Evans\", \"Mark Ruffalo\"]}";
        
        // synthesize the prompt
        String prompt = prompt_part1 + "\n" + "<Actual Question>\n" + "plot=" + plot +", genres=" + genres + ", stars=" + stars + ", directors=" + directors;

        // make request and store the answer
        ChatRequest request = new ChatRequest(model, prompt);
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        String answer = response.getChoices().get(0).getMessage().getContent();
        
        return ResponseEntity.ok(answer);
    }
}