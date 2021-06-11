# Pet Clinic

## Postman collection:

We provide Postman collection which facilitates good and ready requests:

[![Run in Postman](https://run.pstmn.io/button.svg)](https://www.getpostman.com/collections/d19831a69fd748d4ffd3)

## Description:

### System:

System provides features used by pet clinic. It enables user to log in, create an animal and book a visit. A vet can
update visit with changing its state and description. Admin is able to manage all the accounts, and some features.

### Deployment:

Application is deployed automatically using Gitlab-ci, the pipeline is with stages:

- unit tests and build
- integration tests using Postgre database created in Docker
- deploy application on Heroku.

### Security:

Project provides features involving proper security implementation:

- Creating new account
- Logging with receiving JWT token
- Logout with JWT blacklisting
- Endpoints are secured against using without authentication, with wrong role or access to other user's resources. In
  that case 401 is returned.
- Account could be blocked. In that case during login 403 would be returned.

### Used technologies

We used:

- Gradle
- Gitlab-Ci
- Docker
- Micrometer
- Hateoas
- Request validation
- Flyway
- Spring Data JPA
- Mapstruct
- JSONWebToken
- MockMVC, Mockito, AssertJ, JUnit5 for testing
