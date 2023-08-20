package com.ng.fortifytogo.controllers;

import com.ng.fortifytogo.services.GitService;
import jakarta.servlet.http.HttpSession;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String generateReport(@RequestParam String branch, @RequestParam String repoName, @RequestParam String versionNumber, Model model) throws GitAPIException {
        String repoURL = (String) session.getAttribute("repoURL");
        String repoPath = "C:\\Users\\aljos\\OneDrive\\Desktop\\";
        repoName = repoName.isEmpty() ? gitService.getRepoName(repoURL) : repoName;
        repoPath += repoName;

        String statusMessage = gitService.cloneRepository(repoURL, branch, repoPath);

        System.out.println("Branch: " + branch);
        System.out.println("Repo: " + repoName);
        System.out.println("Version: " + versionNumber);
        System.out.println(statusMessage);

        session.setAttribute("branch", branch);
        session.setAttribute("repoName", repoName);
        session.setAttribute("repoVersion", versionNumber);
        model.addAttribute("repoURL", repoURL);
        model.addAttribute("branch", branch);
        model.addAttribute("repoName", repoName);
        model.addAttribute("repoVersion", versionNumber);

        return "report";
    }

    @GetMapping(path="/generateReport")
    public String showReport(Model model) {
        String repoURL = (String) session.getAttribute("repoURL");
        String branch = (String) session.getAttribute("branch");
        String repoName = (String) session.getAttribute("repoName");
        String repoVersion = (String) session.getAttribute("repoVersion");

        if (repoURL == null || branch == null) {
            return "redirect:/";  // Redirect to home if the data is not in session
        }

        model.addAttribute("repoURL", repoURL);
        model.addAttribute("branch", branch);
        model.addAttribute("repoName", repoName);
        model.addAttribute("repoVersion", repoVersion);

        return "report";
    }

}
// TODO: Loading icon
