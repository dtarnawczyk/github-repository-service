# GitHub Repository Service

REST service for retrieving GitHub repository details based on owner and repository name.

### Cloning repository
```sh
$ git clone https://dominik_ta@bitbucket.org/dominik_ta/githubrepositoryservice.git
```
```sh
$ cd githubrepositoryservice
```
### Building executable jar
```sh
$ gradlew release
```
### Running jar
```sh
$ java -jar githubRepositoryService-0.0.1.jar
```

### Using
Service URL: 
```sh
http://localhost:8080/repositories/{owner}/{repository-name}
```
Example usage:
```sh
curl http://localhost:8080/repositories/{owner}/{repository-name}
```

## Tests
```sh
$ gradlew test
```

### Configuration

#### src/main/resources/application.yml

```sh
server:
    port: 8080

logging:
    level:
        ROOT: INFO

github:
    api:
        url: https://api.github.com/repos
        timeout: 5000

```