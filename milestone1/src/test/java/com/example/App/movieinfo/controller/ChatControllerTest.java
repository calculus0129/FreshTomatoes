package com.example.App.movieinfo.controller;

import com.example.App.movieinfo.dto.ChatRequest;
import com.example.App.movieinfo.dto.ChatResponse;
import com.example.App.movieinfo.dto.Message;
import com.example.App.movieinfo.config.OpenAIRestTemplateConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatControllerTest{

    private static final Logger logger = LoggerFactory.getLogger(ChatControllerTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testChatValid() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/chat?plot=they save the earth&genre=action&actor=Chris Evans&bgm=I don't remember",
                String.class);
        // test for valid chat
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testChatInvalid() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/chat?plot=they save the earth&genre=action&actor=Chris Evans",
                String.class);
        // test for invalid chat
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testChatInvalidChat() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/chat?plot=they save the earth&genre=action&actor=Chris Evans&bgm=I don't remember. 느낌표 마지막에 붙여주고",
                String.class);
        // test for invalid chat
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);  // but HttpStatus is OK
    }
}