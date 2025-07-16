package com.example.github_api.model;

import java.util.List;

public record RepositoryResponse(String repositoryName, String ownerLogin, List<BranchResponse> branches) {
}