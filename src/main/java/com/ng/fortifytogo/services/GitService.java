package com.ng.fortifytogo.services;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.*;

@Service
public class GitService {

    public Map<String, Integer> fileCounts = new HashMap<>(Map.of(
            "java", 0,
            "py", 0,
            "js", 0,
            "ts", 0,
            "go", 0,
            "php", 0,
            "rb", 0,
            "cs", 0,
            "c", 0,
            "cpp", 0
    ));

    public String cloneRepository(String repoUrl, String branch, String destinationDir) throws GitAPIException {
        try (Git git = Git.cloneRepository()
                .setURI(repoUrl)
                .setBranch(branch)
                .setDirectory(new File(destinationDir))
                .call()) {
            return "Repository cloned successfully to: " + git.getRepository().getDirectory().getAbsolutePath();
        } catch (GitAPIException e) {
            return "Error cloning repository: " + e.getMessage();
        }
    }

    public void languages(String repoPath) {
        File repo = new File(repoPath);
        if (!repo.exists()) {
            System.out.println("Repository does not exist.");
            return;
        }
        countFiles(repo);
        System.out.println(fileCounts);
    }

    private void countFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            assert files != null;
            for (File f : files) {
                countFiles(f);
            }
        } else {
            String fileName = file.getName();
            String extension = "";

            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                extension = fileName.substring(i + 1);
            }

            if (fileCounts.containsKey(extension)) {
                int count = fileCounts.get(extension);
                fileCounts.put(extension, count + 1);
            }
        }
    }

    public List<String> getBranches(String repoUrl) throws GitAPIException {
        List<String> branches = new ArrayList<>();
        Collection<Ref> refs = Git.lsRemoteRepository().setHeads(true).setRemote(repoUrl).call();
        for (Ref ref : refs) {
            branches.add(ref.getName().replace("refs/heads/", ""));
        }
        return branches;
    }

    public String getRepoName(String repoUrl) {
        if (repoUrl == null || repoUrl.isEmpty()) {
            return null;
        }

        // Remove trailing slashes
        repoUrl = repoUrl.endsWith("/") ? repoUrl.substring(0, repoUrl.length() - 1) : repoUrl;
        // Remove ".git" if present
        repoUrl = repoUrl.endsWith(".git") ? repoUrl.substring(0, repoUrl.length() - 4) : repoUrl;

        // Split the URL by slashes and get the last part
        String[] parts = repoUrl.split("/");
        return parts.length > 0 ? parts[parts.length - 1] : null;
    }

}
