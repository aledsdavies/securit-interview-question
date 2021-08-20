# Implementation Design Document

This document covers the basic of running the application locally in your development environment and also how you would
go about setting up production. I will also document my design decision in here.

Note this implements the beta functionality as I ran out of time that I could commit to complete it further.

## Running in Development

Everything is configured in the git repository and can be run from the gradle commands.

- ./gradlew test - This runs all the tests that have been created
- ./gradlew bootRun - This runs the spring application using the dev application properties at <projectRoot>/config/
- ./gradlew build - Builds a production ready jar

## Running in Production

Once a production jar is complete you will need to set the following properties

- encryption.initVector - Sets the AES initialisation vector to set the initial state
- encryption.key - An aes encryption key used by the EncryptionService to encrypt and decrypt safebox content

Additional to those two required values you will want to configure spring to use a different database for production. I
have added the runtime for postgres into the gradle configuration but this can be switched out for any database that
spring boot JPA and liquibase support.

## Design Decisions

### Gradle

I decided to run everything through gradle. This includes the following

- Setting up the java version - gradle maintains the java version we are using (11). This maintains a consistent
  development environment without users having to set up their pc before running the application. Java 11 was chosen
  because it is the latest long terms support, and it balances new features with support.
- I also choose to take advantage of the swagger configurations and setup gradle to generate controller interfaces and
  models based on what was defined in the specs. (Note that these where read only otherwise I would have changed the
  names of the models which have been generated. Things like InlineObject1 and InlineResponse2001 are a limitation of
  the test)

## General Structure

- The architecture I have aimed for is to follow SOlID principles.
- The general structure of the spring application is as you would expect controllers <- services <- repositories.
- These are mocked out using interfaces with each layer only knowing about the interface of a lower layer and not the
  implementation.

### Controller and Controller Tests

- The controller implements the interface generated from the open api specifications. This saves quite a bit of work
  later when it comes to implementation and also has many benefits for later maintenance of the swagger documents. Any
  changes to the swagger documents will be reflected in the generated interface and will then show compile errors in the
  implementing controller
- The bulk of the testing is done on the api endpoint defined in the controller. I took a TDD approach. I implemented
  each endpoint in a separate test class this not only make it easier to see which endpoints tests are failing but also
  more readable as functionality grows.
- The controller tests implement each expected response in both error cases and success cases to ensure that we are
  conforming to the api contracts set out in the swagger specs.

### Validation

- I was restricted from editing the swagger documents otherwise I could have done most of my validation using spring
  built in validator.
- Where I wasn't able to add more validations I created some private functions in the Controller to handle things like
  checking the id is a valid format etc
- I am also using an external library for checking the password strength. I isolated this into a service so that the
  password check can independent of the library used in case we want to switch it out at a later date.

### Error Handling

- The app tries to throw the majority of its errors form the controller layer. This is to try and stop errors form
  bubbling form any lower layers.
- In most instance I use a custom error which inherits form Exception. This make it clearer what caused the error and
  gives us the context of the error in relation to the app.
- I also have a defined error message format which is returned as JSON in the event of an error.
- Errors are handled globally using spring @ControllerAdvice. This allows me to act based on errors and return the
  messages format as well as the correct status code in a single location reducing duplication.

### Endpoint Security

- Spring security is used to secure every endpoint by default (excluding the post /safebox).
- Auth configurations are in the config package and includes the following
    - WebSecurityConfig - central location where all the security is configured
    - SafeboxAuthenticationService - this implements UserServiceDetails and allows us to authenticate against safebox
      credential stored in the database

## Encryption

- Password - is encrypted using a one way encryption using the Bcrypt algorithm.
- Safebox Contents - is encrypted before being saved to the database using AES encryption. It is then decrypted when the
  content is being read. This is done using the EncryptionService.

### Database Layer

- I wanted to keep the application database agnostic so that it could be switched out at a later date. I use two
  technologies for maintaining the database Spring Data and Liquibase.
    - Liquibase - allows us to version the database in a provider agnostic yaml file.
    - Spring Data - Lets us define data entities using JPA annotations as well as define that repository layers as just
      interfaces. Spring implements these interface for us so saves a lot of work not having to implement query, DTO
      mapping etc