package com.example.App;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebAppTest {

    @Test
    void suggestActivity() {
        WebApp.suggestActivity("Monday", 10); // Should print out coverage of 50%. 40%: statement coverage

    }
}