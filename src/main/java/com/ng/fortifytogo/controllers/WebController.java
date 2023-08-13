package com.ng.fortifytogo.controllers;

import com.ng.fortifytogo.services.GitService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class WebController {

    private final GitService gitService = new GitService();
    private String repoURL;

    @GetMapping(path="/" )
    public String home(){
        return "home";
    }

    @PostMapping(path="/selectBranch")
    public String selectBranch(@RequestParam("repoURL") String repositoryURL, Model model) throws GitAPIException {
        repoURL = repositoryURL;
        List<String> branches = gitService.getBranches(repoURL);
        System.out.println(repoURL);

        for (String b: branches){
            System.out.println(b);
        }

        model.addAttribute("repoBranches", branches);
        return "home";
    }

    @PostMapping(path="/generateReport")
    public String generateReport(@RequestParam String branch, Model model) throws GitAPIException {
        System.out.println("This IS THE BRANCH YOU SELECTED!!!");
        System.out.println(branch);
//        String localRepoPath = gitService.cloneRepository(repoURL, "C:\\Users\\aljos\\OneDrive\\Desktop\\AlexShare");
//        String localRepoPath = gitService.cloneRepository(repoURL, "C:\\Users\\aljos\\OneDrive\\Desktop\\AlexShare");
//        System.out.println(localRepoPath);
        model.addAttribute("repoURL", repoURL);
        model.addAttribute("branch", branch);
        return "report";
    }

}
