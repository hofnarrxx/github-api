package com.example.github_api.model;

import java.util.List;

public record Repository(String repositoryName, String ownerLogin, List<RepositoryBranch> branches) {
}