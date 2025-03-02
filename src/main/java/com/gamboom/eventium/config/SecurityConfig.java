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
///**/         http
///**/                 .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (enable it in production)
///**/                .authorizeHttpRequests(auth -> auth
///**/                        .requestMatchers("/", "/login**", "/error**", "/swagger-ui/**", "/v3/api-docs/**").permitAll() // Public endpoints
///**/                        .requestMatchers("/api/events/create", "/api/events/update/**", "/api/events/delete/**").hasRole("STAFF") // Staff-only endpoints
///**/                        .anyRequest().authenticated() // All other endpoints require authentication
///**/                )
///**/                .oauth2Login(oauth2 -> oauth2
///**/                        .loginPage("/login") // Use custom login page
///**/                        .defaultSuccessUrl("/home", true) // Redirect after successful login
///**/                )
///**/                .logout(logout -> logout
///**/                        .logoutUrl("/logout") // Specify the logout URL
///**/                        .logoutSuccessUrl("/login") // Redirect to home page after logout
///**/                        .invalidateHttpSession(true) // Invalidate the session
///**/                        .deleteCookies("JSESSIONID") // Delete cookies
///**/                        .permitAll() // Allow everyone to access the logout endpoint
///**/                );
/* ********************************************************************************************************** */

/* Temporarily turns off GitHub OAuthentication for testing purposes */
/**/        http
/**/                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Allow all requests
/**/                .csrf(csrf -> csrf.disable())  // Disable CSRF for Postman testing
/**/                .headers(headers -> headers.disable()); // ✅ Fully disable headers, replacing frameOptions()
/* ***************************************************************** */

        return http.build();
    }
}