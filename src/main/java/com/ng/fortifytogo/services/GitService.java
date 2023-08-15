package com.ng.fortifytogo.services;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class GitService {

    public String cloneRepository(String repoUrl, String branch, String destinationDir) throws GitAPIException {
        try (Git git = Git.cloneRepository()
                .setURI(repoUrl)
                .setBranch(branch)
                .setDirectory(new File(destinationDir))
                .call()) {
            return git.getRepository().getDirectory().getAbsolutePath();
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
