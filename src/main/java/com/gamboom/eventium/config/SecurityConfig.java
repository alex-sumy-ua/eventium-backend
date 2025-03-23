package com.gamboom.eventium.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   GitHubTokenAuthenticationFilter gitHubFilter) throws Exception {

        /* ************* Turn on GitHub OAuthentication if needed *************************************************** */
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
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/redirect-to-app", true)
                        .failureUrl("/login?error=true")
                        .authorizationEndpoint(authz ->
                                authz.baseUri("/oauth2/authorization"))
                        .redirectionEndpoint(redir ->
                                redir.baseUri("/login/oauth2/code/*"))
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        // 🔒 Add token-based authentication before standard username-password auth
        http.addFilterBefore(gitHubFilter, UsernamePasswordAuthenticationFilter.class);
        /* ********************************************************************************************************** */
        /* Temporarily turns off GitHub OAuthentication for testing purposes */
        //http
        //        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Allow all requests
        //        .csrf(csrf -> csrf.disable())  // Disable CSRF for Postman testing
        //        .headers(headers -> headers.disable()); // Fully disable headers, replacing frameOptions()
        /* ********************************************************************************************************** */

        return http.build();
    }
}
