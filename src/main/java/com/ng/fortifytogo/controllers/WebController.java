package com.ng.fortifytogo.controllers;

import com.ng.fortifytogo.services.GitService;
import jakarta.servlet.http.HttpSession;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    @Autowired
    private HttpSession session;
    private final GitService gitService = new GitService();
    private String repoURL;

    @GetMapping(path="/" )
    public String home(){
        return "home";
    }

    @PostMapping(path="/generateReport")
    public String generateReport(@RequestParam String branch, Model model) throws GitAPIException {
        System.out.println("This IS THE BRANCH YOU SELECTED!!!");
        System.out.println(branch);
        String repoURL = (String) session.getAttribute("repoURL");
        String localRepoPath = gitService.cloneRepository(repoURL, branch, "C:\\Users\\aljos\\OneDrive\\Desktop\\TestRepo");
        System.out.println(localRepoPath);
        model.addAttribute("repoURL", repoURL);
        model.addAttribute("branch", branch);
        return "report";
    }

}
