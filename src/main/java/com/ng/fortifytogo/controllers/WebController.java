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
    public String generateReport(@RequestParam String branch, Model model) throws GitAPIException {
        System.out.println("This IS THE BRANCH YOU SELECTED!!!");
        System.out.println(branch);
        String repoURL = (String) session.getAttribute("repoURL");
        session.setAttribute("branch", branch);
//        String localRepoPath = gitService.cloneRepository(repoURL, branch, "C:\\Users\\aljos\\OneDrive\\Desktop\\TestRepo");
//        System.out.println(localRepoPath);
        model.addAttribute("repoURL", repoURL);
        model.addAttribute("branch", branch);
        return "report";
    }

    @GetMapping(path="/generateReport")
    public String showReport(Model model) {
        String repoURL = (String) session.getAttribute("repoURL");
        String branch = (String) session.getAttribute("branch");

        if (repoURL == null || branch == null) {
            return "redirect:/";  // Redirect to home if the data is not in session
        }

        model.addAttribute("repoURL", repoURL);
        model.addAttribute("branch", branch);

        return "report";
    }

}
// TODO: Loading icon
// TODO: Report download, view
// TODO: Combine to one page