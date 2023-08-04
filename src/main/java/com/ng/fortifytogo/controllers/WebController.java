package com.ng.fortifytogo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    @GetMapping(path="/" )
    public String home(){
        return "home";
    }

    @PostMapping(path="/generateReport")
    public String genReport(@RequestParam("githubLink") String githubLink, Model model){
        System.out.println(githubLink);
        model.addAttribute("githubLink", githubLink);
        return "report";
    }


}
