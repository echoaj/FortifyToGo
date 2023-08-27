package com.ng.fortifytogo.controllers;

import com.ng.fortifytogo.services.FortifyService;
import com.ng.fortifytogo.services.GitService;
import jakarta.servlet.http.HttpSession;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Controller
public class WebController {

    @Autowired
    private HttpSession session;
    private final GitService gitService = new GitService();
    private final FortifyService fortService = new FortifyService();
    private String repoURL;

    @GetMapping(path="/" )
    public String home(){
        return "home";
    }

    @PostMapping(path="/generateReport")
    public String generateReport(@RequestParam String branch, @RequestParam String repoName, @RequestParam String versionNumber, Model model) throws GitAPIException, IOException {
        String repoURL = (String) session.getAttribute("repoURL");
        String repoPath = "C:\\Users\\aljos\\OneDrive\\Desktop\\";
        repoName = repoName.isEmpty() ? gitService.getRepoName(repoURL) : repoName;
        repoPath += repoName;

        String gitStatusMessage = gitService.cloneRepository(repoURL, branch, repoPath);
        System.out.println(gitStatusMessage);
        gitService.languages(repoPath);
        String reportName = fortService.scan(repoName, versionNumber, repoPath);

        System.out.println("Branch: " + branch);
        System.out.println("Repo: " + repoName);
        System.out.println("Version: " + versionNumber);
        System.out.println("Report Name: " + reportName);

        session.setAttribute("branch", branch);
        session.setAttribute("repoName", repoName);
        session.setAttribute("repoVersion", versionNumber);
        session.setAttribute("reportName", reportName);
        session.setAttribute("langMap", gitService.fileCounts);
        model.addAttribute("repoURL", repoURL);
        model.addAttribute("branch", branch);
        model.addAttribute("repoName", repoName);
        model.addAttribute("repoVersion", versionNumber);
        model.addAttribute("reportName", reportName);
        model.addAttribute("langMap", gitService.fileCounts);

        return "report";
    }

    // This function gets called after generateReport returns report.html.  The Iframe makes a call to this endpoint
    @GetMapping("/reportFile/{fileName}")
    public ResponseEntity<Resource> getReportFile(@PathVariable String fileName) throws IOException {
        Path file = Paths.get("C:\\Users\\aljos\\OneDrive\\Documents\\Java\\fortifytogo\\fortifytogo\\src\\main\\resources\\static", fileName);
        Resource resource = new InputStreamResource(Files.newInputStream(file));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping(path="/generateReport")
    public String showReport(Model model) {
        String repoURL = (String) session.getAttribute("repoURL");
        String branch = (String) session.getAttribute("branch");
        String repoName = (String) session.getAttribute("repoName");
        String repoVersion = (String) session.getAttribute("repoVersion");
        String reportName = (String) session.getAttribute("reportName");
        Map<String, Integer> fileCounts = (Map<String, Integer>) session.getAttribute("langMap");

        if (repoURL == null || branch == null) {
            return "redirect:/";  // Redirect to home if the data is not in session
        }

        model.addAttribute("repoURL", repoURL);
        model.addAttribute("branch", branch);
        model.addAttribute("repoName", repoName);
        model.addAttribute("repoVersion", repoVersion);
        model.addAttribute("reportName", reportName);
        model.addAttribute("langMap", fileCounts);

        return "report";
    }

}
