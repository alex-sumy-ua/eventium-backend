package com.gamboom.eventium.config;

import com.gamboom.eventium.model.User;
import com.gamboom.eventium.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class GitHubTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(GitHubTokenAuthenticationFilter.class);

    private final GitHubUserService gitHubUserService;
    private final UserRepository userRepository;

    public GitHubTokenAuthenticationFilter(GitHubUserService gitHubUserService,
                                           UserRepository userRepository) {
        this.gitHubUserService = gitHubUserService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();  // remove "Bearer "

            logger.info("Checking token from header: {}", token);

            // Step 1: Find email from token
            String email = gitHubUserService.getEmailByToken(token);  // NEW helper method

            if (email != null) {
                Optional<User> userOpt = userRepository.findByEmail(email);

                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    logger.info("Authenticated user: {}", user.getEmail());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.warn("No user found in DB for email: {}", email);
                }
            } else {
                logger.warn("Token not found in backend store: {}", token);
            }
        }

        filterChain.doFilter(request, response);
    }

}
