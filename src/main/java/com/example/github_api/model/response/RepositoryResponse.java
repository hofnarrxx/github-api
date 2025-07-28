package com.example.github_api.model.response;

import com.example.github_api.model.Owner;

public record RepositoryResponse(String name, boolean fork, Owner owner) {
    
}
