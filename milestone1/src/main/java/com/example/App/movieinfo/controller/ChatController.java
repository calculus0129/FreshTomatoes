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
        String prompt_part1 = "";
        try {
            prompt_part1 = readFileAsString("./src/main/resources/prompt_part1.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // synthesize the prompt
        String prompt = prompt_part1 + "\n" + "<Actual Question>\n" + "plot=" + plot +", genres=" + genres + ", stars=" + stars + ", directors=" + directors;

        // make request and store the answer
        ChatRequest request = new ChatRequest(model, prompt);
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        String answer = response.getChoices().get(0).getMessage().getContent();
        
        return ResponseEntity.ok(answer);
    }
}