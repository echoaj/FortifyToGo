package com.ng.fortifytogo.controllers;

import com.ng.fortifytogo.services.GitService;
import jakarta.servlet.http.HttpSession;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ApiController {

    @Autowired
    private HttpSession session;
    private final GitService gitService = new GitService();

    @PostMapping(path="/selectBranch")
    public List<String> selectBranch(@RequestBody Map<String, String> data) throws GitAPIException {
        String repoURL = data.get("repoURL");
        session.setAttribute("repoURL", repoURL);
        List<String> branches = gitService.getBranches(repoURL);

        // If you want to print for debugging
        System.out.println("Printing out branches");
        System.out.println(repoURL);
        for (String b: branches){
            System.out.println(b);
        }

        System.out.println("Total Branches:" + branches.size());

        return branches;
    }

}
