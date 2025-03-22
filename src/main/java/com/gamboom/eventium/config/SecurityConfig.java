package com.gamboom.eventium.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

/* ************* Turn on GitHub OAuthentication if needed *************************************************** */
         http
                 .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (enable it in production)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login**", "/oauth2/**", "/error**", "/swagger-ui/**", "/v3/api-docs/**", "/api/users/by-email/**").permitAll() // Public endpoints
                        .requestMatchers("/api/events/update/**", "/api/events/delete/**").hasRole("STAFF") // Staff-only endpoints
                        .anyRequest().authenticated() // All other endpoints require authentication
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/redirect-to-app", true) // Ensures the redirect happens immediately after login
                        .failureUrl("/login?error=true")
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorization")
                        )
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/login/oauth2/code/*") // GitHub redirects back here
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Specify the logout URL
                        .logoutSuccessUrl("/login") // Redirect to home page after logout
                        .invalidateHttpSession(true) // Invalidate the session
                        .deleteCookies("JSESSIONID") // Delete cookies
                        .permitAll() // Allow everyone to access the logout endpoint
                );
/* ********************************************************************************************************** */

/* Temporarily turns off GitHub OAuthentication for testing purposes */
///**/        http
///**/                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Allow all requests
///**/                .csrf(csrf -> csrf.disable())  // Disable CSRF for Postman testing
///**/                .headers(headers -> headers.disable()); // ✅ Fully disable headers, replacing frameOptions()
/* ***************************************************************** */

        return http.build();
    }
}