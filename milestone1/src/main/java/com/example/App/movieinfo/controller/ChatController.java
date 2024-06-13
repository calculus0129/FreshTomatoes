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

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String plot, @RequestParam String genres, @RequestParam String stars, @RequestParam String directors) {
        String content = "";
        try {
            content = readFileAsString("/home/nyw0102/projects-group3/milestone1/src/main/resources/prompt.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String prompt = content + "\n" + "<Actual Question>\n" + "plot=" + plot +", genres=" + genres + ", stars=" + stars + ", directors=" + directors;

        ChatRequest request = new ChatRequest(model, prompt);

        ChatResponse response = restTemplate.postForObject(
                apiUrl,
                request,
                ChatResponse.class);

        String answer = response.getChoices().get(0).getMessage().getContent();
        

        // Refining the answer
        // String modifiedString = answer.replaceAll("^\"```json\\n|\n|\\n```\"$", "");
        // System.out.println(modifiedString);
        return ResponseEntity.ok(answer);
    }
}