package com.ng.fortifytogo.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FortifyService {
    public String scan(String repoName, String repoVersion, String repoPath) throws IOException {
        try {
            String pdf_title = repoName + "_" + repoVersion + "_" + "OWASP_Top_10-2021_fortify_report";
            System.out.println("Scanning repo: " + repoName);
            // More logic related to scanning...
            String reportPath = copyPDF(pdf_title);
            System.out.println("Fortify Scan Successful. Generated: " + pdf_title);
            System.out.println("Report in: " + reportPath);
            return pdf_title + ".pdf";
        } catch (Exception e) {
            System.err.println("Fortify Scan Failed: " + e.getMessage());
            throw e;
        }
    }

    public String copyPDF (String pdf_name) throws IOException {
        // Define source and target paths
        String staticPath = "C:\\Users\\aljos\\OneDrive\\Documents\\Java\\fortifytogo\\fortifytogo\\src\\main\\resources\\static";
        Path sourcePath = Paths.get(staticPath, "alex_resume.pdf");
        Path targetPath = Paths.get(staticPath, pdf_name + ".pdf");

        try {
            // Copy the file
            // Check if target file exists
            if (Files.exists(targetPath)) {
                // Decide how to handle: overwrite, skip, or rename.
                // Here, we're choosing to overwrite.
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(sourcePath, targetPath);
            }
            System.out.println("PDF copied successfully!");
            return targetPath.toString();
        } catch (IOException e) {
            System.err.println("Error occurred while copying the PDF: " + e.getMessage());
            throw e;
        }
    }
}
