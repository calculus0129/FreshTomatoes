package com.example.App.movieinfo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        // Redirect to the actual index.html file, assuming it is served statically
        return "redirect:/index.html";
    }

    @GetMapping("/mvfinder")
    public String mvfinder() {
        return "redirect:/mvfinder/index.html";
    }
    @GetMapping("/vis")
    public String vis() {
        return "redirect:/vis/index.html";
    }
    @GetMapping("/rec")
    public String rec() {
        // This specifies that when a user visits http://localhost:8080/rec,
        // they should be redirected to the index.html file inside the rec directory.
        return "redirect:/rec/index.html";
    }
}