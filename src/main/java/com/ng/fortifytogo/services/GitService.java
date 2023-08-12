package com.ng.fortifytogo.services;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class GitService {

    public String cloneRepository(String repoUrl, String destinationDir) throws GitAPIException {
        try (Git git = Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(new File(destinationDir))
                .call()) {
            return git.getRepository().getDirectory().getAbsolutePath();
        }
    }

}
