package com.example.github_api;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import org.springframework.web.server.ResponseStatusException;

import com.example.github_api.model.RepositoryBranch;
import com.example.github_api.model.response.RepositoryBranchResponse;
import com.example.github_api.model.response.RepositoryResponse;
import com.example.github_api.model.Repository;

@Service
public class GithubService {
    private final RestClient restClient;

    public GithubService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://api.github.com")
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("User-Agent", "Github-API")
                .build();
    }

    public List<Repository> getRepositories(String username) {
        List<RepositoryResponse> repositories = restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        if (repositories == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "GitHub API returned incorrect data for user: " + username);
        }

        if (repositories.isEmpty())
            return List.of();

        return repositories.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> new Repository(
                        repo.name(),
                        repo.owner().login(),
                        getBranches(repo.owner().login(), repo.name())))
                .toList();
    }

    private List<RepositoryBranch> getBranches(String owner, String repoName) {
        List<RepositoryBranchResponse> branches = restClient.get()
                .uri("/repos/{owner}/{repo}/branches", owner, repoName)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (branches == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "GitHub API returned incorrect data for repository: " + repoName);
        }

        if (branches.isEmpty())
            return List.of();

        return branches.stream()
            .map(branch -> new RepositoryBranch(branch.name(),
                branch.commit().sha()))
            .toList();
    }
}
