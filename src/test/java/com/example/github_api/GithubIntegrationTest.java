package com.example.github_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.github_api.model.BranchResponse;
import com.example.github_api.model.RepositoryResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GithubIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnRepositoriesWithBranchesForValidGithubUser() {
        // Given
        String githubUsername = "octocat";

        // When
        ResponseEntity<List<RepositoryResponse>> response = restTemplate.exchange(
            "/repositories/"+githubUsername,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<RepositoryResponse> repositories = response.getBody();
        assertThat(repositories).isNotNull();
        assertThat(repositories.size()).isGreaterThan(0);

        for (RepositoryResponse repo : repositories) {
            assertThat(repo.repositoryName()).isNotBlank();
            assertThat(repo.ownerLogin()).isEqualTo(githubUsername);
            assertThat(repo.branches()).isNotNull();

            for (BranchResponse branch : repo.branches()) {
                assertThat(branch.name()).isNotBlank();
                assertThat(branch.lastCommitSha()).isNotBlank();
            }
        }
    }
}