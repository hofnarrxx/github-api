package com.example.github_api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.example.github_api.model.RepositoryResponse;

@RestController
@RequestMapping("/repositories")
public class RepositoryController {
    @Autowired
    private GithubService githubService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserRepositories(@PathVariable String username) {
        try {
            List<RepositoryResponse> repositories = githubService.getRepositories(username);
            return ResponseEntity.ok(repositories);
        } catch (HttpClientErrorException.NotFound e) {
            Map<String, Object> errorBody = Map.of(
                    "status", 404,
                    "message", "GitHub user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
        }
    }
}
