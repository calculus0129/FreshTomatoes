package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.dto.ChatRequest;
import com.example.App.movieinfo.dto.ChatResponse;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Creates a chat request and sends it to the OpenAI API
     * Returns the first message from the API response
     *
     * @param prompt the prompt to send to the API
     * @return first message from the API response
     */
    @GetMapping("/chat")
    public String chat(@RequestParam String plot, @RequestParam String genre, @RequestParam String actor, @RequestParam String bgm) {
        String prompt = "Please tell me the names of this movies. Here are some informations about movies that I can remember: "
                + "{" + "plot= " + plot + "genre= " + genre + "actor= " + actor + "bgm= " + bgm + "}"
                + "Please respond in this format: [name_1, name_2, ..., name_n]";

        ChatRequest request = new ChatRequest(model, prompt);

        ChatResponse response = restTemplate.postForObject(
                apiUrl,
                request,
                ChatResponse.class);

        // if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
        //     return "No response";
        // }

        String answer = response.getChoices().get(0).getMessage().getContent();

        // Refining the answer

        String regex = "^\\[.*\\]$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(answer);

        if(matcher.matches()){
            String new_regex = "\\[(.*?)\\]";
            Pattern new_pattern = Pattern.compile(new_regex);
            Matcher new_matcher = new_pattern.matcher(answer);

            String[] names = null;
            String holder = "";
            int i = 1;

            if (new_matcher.find()) {
                String matched = new_matcher.group(1);
                names = matched.split(",\\s*");

                // for (String name:names) {
                //     holder += "movie " + Integer.toString(i) + " name: " + name + "\n";
                //     i++;
                // }
                for (String name:names) {
                    holder += "movie " + Integer.toString(i) + " name: " + name;
                    if (i != names.length) {
                        holder += ", ";
                    }
                    i++;
                }
            }

            answer = holder;
        }
        return answer;
    }
}