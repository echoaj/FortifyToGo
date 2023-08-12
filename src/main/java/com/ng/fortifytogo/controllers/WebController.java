package com.ng.fortifytogo.controllers;

import com.ng.fortifytogo.services.GitService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final GitService gitService = new GitService();

    @GetMapping(path="/" )
    public String home(){
        return "home";
    }

    @PostMapping(path="/generateReport")
    public String genReport(@RequestParam("githubLink") String githubLink, Model model) throws GitAPIException {
        System.out.println(githubLink);
        String localRepoPath = gitService.cloneRepository(githubLink, "C:\\Users\\aljos\\OneDrive\\Desktop\\AlexShare");
        System.out.println(localRepoPath);
        model.addAttribute("githubLink", githubLink);
        return "report";
    }


}
