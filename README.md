Events Platform EVENTIUM

A small community business can create and share events with members of the community.
A mobile app that allows community members to view, sign up for, and add events to their own personal calendars.
Staff members have additional functionality to create and manage events.



Minimum Viable Product (MVP)

The platform fulfils the following functionality:

1. Display a list of events for users to browse.

2. Allow users to sign up for an event.

3. Allow users to add events to their Google Calendar after signing up.

4. Enable staff members to sign-in to create and manage events.



Tech Choices:

• The platform should be built using Java or Kotlin.

• Event Data: You can use either a freely available API for event data or create your own event data.

  Research and decide on which API to use prior to starting. The focus is on building the platform, not on data generation.
  
• Calendar API: You'll need to sign up for the Google Calendar API (or an equivalent) using a free developer account. This will allow users to add events to their calendars.

• Implement security best practices for user authentication using OAuth login flow/username and password (e.g., Google Sign-In, GitHub OAuth)



The following technologies and tools are suggestions, not requirements:

• Android for the frontend.
• Kotlin for a new challenge.
• Google Calendar API for calendar integration.
• Google Sign-In for OAuth social sign in


Backend technologies, sub-technologies and tools:

• IntelliJ Idea

• Maven

• Java 17

• Java EE

• GitHub

• NeonDB

• https://dbdiagram.io/

• JDBC

• PostgreSQL

• Spring Boot, https://start.spring.io/

• JUnit

• GitHub OAuth2

• PGAdmin

• Postman

• Swagger

• OpenAPI

• Google Calendar



UI Requirements


• Design should be responsive and adapt well across various mobile device screen sizes.

• Ensure accessibility for users with disabilities (e.g., support screen readers, voice navigation).

• The UI should clearly display errors (e.g., failed requests or missing fields) and show loading states when content is being fetched.

• The user interface should be intuitive, making it easy to find, sign up for, and create events.



Optional Extensions


If you have time once you have completed the MVP requirements, consider adding the following features:

1. Payment platform integration: Implement payments via Stripe, Google Pay, etc.

2. Confirmation emails: Automatically send confirmation emails to users who sign up for an event.

3. Social media integration: Allow users to share events on social platforms.

4. Cross-platform: Build both a mobile app and website.

5. Spring Security implement full authentication using the Spring Security framework for secure communication between the backend API and frontend Android app.


======================================================================

Backend

======================================================================

Database


DB Diagram version 1: https://dbdiagram.io/d/eventium-db-v-1-67a1434a263d6cf9a0e82cd3


![image](https://github.com/user-attachments/assets/2ca705bd-f830-4f4a-a139-533c47a9aec1)



SQL-scripts - tables creating

-- Create ENUM type for user roles
CREATE TYPE user_role AS ENUM ('admin', 'staff', 'member');

-- Create users table with UUID primary key
CREATE TABLE users (
user_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
name VARCHAR(255),
email VARCHAR(255) UNIQUE,
password VARCHAR(255),
role user_role,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create events table with UUID primary key
CREATE TABLE events (
event_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
title VARCHAR(255),
description TEXT,
location VARCHAR(255),
start_time TIMESTAMP,
end_time TIMESTAMP,
created_by UUID REFERENCES users(user_id) ON DELETE CASCADE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create event_registrations table with UUID primary key and foreign keys
CREATE TABLE event_registrations (
event_registration_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
user_id UUID REFERENCES users(user_id) ON DELETE CASCADE,
event_id UUID REFERENCES events(event_id) ON DELETE CASCADE,
registration_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT unique_registration UNIQUE (user_id, event_id)
);

-- Create an index for faster queries on event start times
CREATE INDEX idx_event_start_time ON events(start_time);


===============================================================================


Java Code Base

Model,
Service, Controller, Repository layers,
GlobalExceptionHandler have been developed.


The fully documented backend API application can be seen while the application is running:
- http://localhost:8080/v3/api-docs (simple JSON format);
- http://localhost:8080/swagger-ui/index.html (more readable format).


![image](https://github.com/user-attachments/assets/42f1d227-f772-4599-a28a-d011eb1b7db8)


![image](https://github.com/user-attachments/assets/64a7a221-8083-4576-ab56-9be1efaad7bc)


![image](https://github.com/user-attachments/assets/9184bdad-ad0a-4e7f-a4b7-fa23ffc24bb9)


===============================================================================


GitHub OAuth2 Authentication


This project uses GitHub OAuth2 for user authentication. Users can log in using their GitHub accounts, and the application securely retrieves their profile information (e.g., name and email).

Steps to Implement GitHub OAuth2
Register the Application on GitHub:

Go to GitHub Developer Settings.

Create a new OAuth app with the following details:

Homepage URL: http://localhost:8080

Authorization callback URL: http://localhost:8080/login/oauth2/code/github

Save the Client ID and Client Secret.

Configure GitHub OAuth in application.properties:

spring.security.oauth2.client.registration.github.client-id=your-client-id
spring.security.oauth2.client.registration.github.client-secret=your-client-secret
spring.security.oauth2.client.registration.github.scope=read:user,user:email
spring.security.oauth2.client.registration.github.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}
Enable OAuth2 in Spring Security:

Configure SecurityConfig to enable OAuth2 login and secure endpoints:

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
http
.csrf(csrf -> csrf.disable())
.authorizeHttpRequests(auth -> auth
.requestMatchers("/", "/login**", "/error**").permitAll()
.anyRequest().authenticated()
)
.oauth2Login(oauth2 -> oauth2
.loginPage("/login")
.defaultSuccessUrl("/home", true)
)
.logout(logout -> logout
.logoutUrl("/logout")
.logoutSuccessUrl("/")
.permitAll()
);
return http.build();
}


Create Login and Home Pages:

login.html: Provides a button to log in with GitHub.

home.html: Displays the user’s name and email after successful login.

Handle User Details:

Use the @AuthenticationPrincipal OAuth2User annotation in the HomeController to retrieve user details:

@GetMapping("/home")
public String home(Model model, @AuthenticationPrincipal OAuth2User principal) {
if (principal != null) {
model.addAttribute("name", principal.getAttribute("name"));
model.addAttribute("email", principal.getAttribute("email"));
}
return "home";
}


Test the Integration:

Start the application and navigate to http://localhost:8080.

Click Login with GitHub to authenticate.

After logging in, you’ll be redirected to the home page, which displays your GitHub profile details.

Key Features
Secure Authentication: Uses GitHub OAuth2 for secure user login.

User Details: Retrieves and displays the user’s name and email.

Logout Functionality: Allows users to log out and redirects them to the home page.

Dependencies

spring-boot-starter-oauth2-client

spring-boot-starter-security

spring-boot-starter-thymeleaf (for rendering HTML templates)

Usage

Log in with GitHub.

View your profile details on the home page.

Log out when done.










