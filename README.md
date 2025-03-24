# EVENTIUM - Backend


A Spring Boot backend for a community event platform that allows users to view and register for events, while staff members can manage events. It also integrates with GitHub OAuth2 for secure user authentication and supports adding events to Google Calendar via the frontend.


## ✨ Project Summary

EVENTIUM is a platform designed for community members to engage with events:

Users can view and register for events.

Users can add events to Google Calendar.

Staff can create, edit, and delete events.

OAuth2 login via GitHub ensures secure authentication.

This backend REST API is built using Java 17, Spring Boot, and PostgreSQL. It is fully documented via Swagger/OpenAPI.


## 📊 Technologies Used

Core Stack

Java 17

Spring Boot

PostgreSQL

Maven


### 📊 Backend Subsystems

Spring Data JPA / JDBC

Spring Security

GitHub OAuth2 Integration

JUnit 5 for testing

Swagger / OpenAPI

RESTful API architecture


### 📊 Dev Tools

IntelliJ IDEA

PGAdmin

Postman

Git & GitHub

NeonDB (PostgreSQL hosting)

dbdiagram.io (for ER diagram)


### 📊 Frontend Tech (paired with backend)

Android Studio (Java)

Google Calendar Intent API

GitHub OAuth2 login (forwarded from frontend)


## 📁 API Documentation

Swagger/OpenAPI UI is available at runtime:

JSON format: http://localhost:8080/v3/api-docs

Swagger UI: http://localhost:8080/swagger-ui/index.html


## 📆 Database Schema

PostgreSQL with the following entities:

- users (user_id, name, email, password, role, created_at)

- events (event_id, title, description, location, start_time, end_time, created_by, created_at)

- event_registrations (event_registration_id, user_id, event_id, registration_time)


### 📊 DB Diagram:
https://dbdiagram.io/d/eventium-db-v-1-67a1434a263d6cf9a0e82cd3


## ✉️ Authentication - GitHub OAuth2

### GitHub Setup

1. Register app at https://github.com/settings/developers

2. Set:

  - Homepage URL: http://localhost:8080

  - Callback URL: http://10.0.2.2:8080/login/oauth2/code/github

```
application.properties
spring.security.oauth2.client.registration.github.client-id=your-client-id
spring.security.oauth2.client.registration.github.client-secret=your-client-secret
spring.security.oauth2.client.registration.github.scope=read:user,user:email
spring.security.oauth2.client.registration.github.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}
```


### 📊 Security Configuration

```
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                               GitHubTokenAuthenticationFilter gitHubFilter) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                             "/"
                            ,"/login**"
                            ,"/oauth2/**"
                            ,"/error**"
                            ,"/swagger-ui/**"
                            ,"/v3/api-docs/**"
                            ,"/api/users/by-email/**"
                    ).permitAll()
                    .anyRequest().authenticated())
            .oauth2Login(oauth2 -> oauth2
                    .loginPage("/login")
                    .defaultSuccessUrl("/redirect-to-app", true)
                    .failureUrl("/login?error=true")
                    .authorizationEndpoint(authz ->
                            authz.baseUri("/oauth2/authorization"))
                    .redirectionEndpoint(redir ->
                            redir.baseUri("/login/oauth2/code/*")))
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll());
            return http.build();
}
```


## 🚀 Running the Backend Locally

### 1. Prerequisites

Java 17

Maven

PostgreSQL (or NeonDB instance)

### 2. Setup

Clone the repo:

git clone https://github.com/your-username/eventium-backend.git
cd eventium-backend

### 3. Configure application.properties or application.yml:

spring.datasource.url=jdbc:postgresql://your-host:5432/eventium
spring.datasource.username=your-db-user
spring.datasource.password=your-db-pass

### 4. Build and run:

mvn clean install
mvn spring-boot:run


##  💪 Features

View all events

Register for events

Only one registration per user per event

Only staff can add, update, or delete events

Full CRUD for events and registrations

GitHub login integration

Swagger UI for API testing


##  ☑️ MVP Complete

This backend meets all MVP requirements:

✅ Event browsing and signup

✅ Secure authentication with GitHub OAuth

✅ API endpoints for event and registration management

✅ Support for frontend features (like Google Calendar)

✅ Fully documented with Swagger


##  ✨ Optional Extensions

✅ Spring Security implement full authentication using the Spring Security framework for secure communication between the backend API and frontend Android app



## 💼 License

This project is for educational/demo purposes only. MIT-style license can be added if publishing publicly.



## 📅 Author

### Oleksandr Plachkovskyi (Northcoders student, consultant)
### plachkovskyy@yahoo.com
