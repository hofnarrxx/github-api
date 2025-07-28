package com.example.github_api.model.response;

import com.example.github_api.model.Commit;

public record RepositoryBranchResponse(String name, Commit commit) {
    
}
