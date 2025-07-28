package com.example.github_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



import com.example.github_api.model.RepositoryBranch;
import com.example.github_api.model.Repository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GithubIntegrationTest {

    @Autowired
    private RepositoryController repositoryController;

    @Test
    void shouldReturnRepositoriesWithBranchesForValidGithubUser() {
        // Given
        String githubUsername = "octocat";

        // When
        List<Repository> repositories = repositoryController.getUserRepositories(githubUsername);

        // Then
        assertThat(repositories).isNotNull();
        assertThat(repositories.size()).isGreaterThan(0);

        for (Repository repo : repositories) {
            assertThat(repo.repositoryName()).isNotBlank();
            assertThat(repo.ownerLogin()).isEqualTo(githubUsername);
            assertThat(repo.branches()).isNotNull();

            for (RepositoryBranch branch : repo.branches()) {
                assertThat(branch.name()).isNotBlank();
                assertThat(branch.lastCommitSha()).isNotBlank();
            }
        }
    }
}