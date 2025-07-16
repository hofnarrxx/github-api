# GitHub Repository API

This is a Spring Boot 3.5 application (Java 21) that provides an endpoint to fetch **non-fork GitHub repositories** for a given user, including their branches and latest commit SHAs.

## Features
- Connects to [GitHub REST API v3](https://developer.github.com/v3/)
- Filters out forked repositories
- Lists:
  - Repository name
  - Owner login
  - Every branch name and last commit SHA
- Returns `404` with custom JSON when GitHub user doesn't exist

## Requirements  
Java 21

## Usage example
- start application
- access http://localhost:8080/repositories/octocat


