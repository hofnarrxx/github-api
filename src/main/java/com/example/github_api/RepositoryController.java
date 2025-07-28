package com.example.github_api;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.github_api.model.Repository;

@RestController
@RequestMapping("/repositories")
public class RepositoryController {
    private GithubService githubService;

    public RepositoryController(GithubService githubService){
        this.githubService = githubService;
    }

    @GetMapping("/{username}")
    public List<Repository> getUserRepositories(@PathVariable String username) {
         return githubService.getRepositories(username);
    }
}
