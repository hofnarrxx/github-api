package com.example.github_api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.github_api.model.BranchResponse;
import com.example.github_api.model.RepositoryResponse;

@Service
public class GithubService {
    @Autowired
    private RestTemplate restTemplate;

    public List<RepositoryResponse> getRepositories(String username) {
        String repoUrl = "https://api.github.com/users/" + username + "/repos";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.valueOf("application/vnd.github+json")));
        headers.set("User-Agent", "Github-API");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                repoUrl,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                });

        List<Map<String, Object>> repositories = response.getBody();
        if (repositories == null) return List.of();

        return repositories.stream()
            .filter(repo -> !(Boolean) repo.get("fork"))
            .map(repo -> {
                String name = (String) repo.get("name");
                Map<String, Object> owner = (Map<String, Object>) repo.get("owner");
                String login = (String) owner.get("login");
                List<BranchResponse> branches = getBranches(login, name);
                return new RepositoryResponse(name, login, branches);
            }).collect(Collectors.toList());
    }

    private List<BranchResponse> getBranches(String owner, String repoName) {
        String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/branches";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.valueOf("application/vnd.github+json")));
        headers.set("User-Agent", "Github-API");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                });

        List<Map<String, Object>> branchList = response.getBody();
        if (branchList == null) return List.of();

        return branchList.stream()
                .map(branch -> {
                    String name = (String) branch.get("name");
                    Map<String, Object> commit = (Map<String, Object>) branch.get("commit");
                    String sha = (String) commit.get("sha");
                    return new BranchResponse(name, sha);
                })
                .collect(Collectors.toList());
    }
}
