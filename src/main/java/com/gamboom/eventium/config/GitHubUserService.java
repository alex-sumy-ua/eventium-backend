package com.gamboom.eventium.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GitHubUserService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubUserService.class);

    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();

    public void saveToken(String email, String token) {
        tokenStore.put(email, token);
        logger.info("Saved token for user {}: {}", email, token);
    }

    public String getTokenByEmail(String email) {
        return tokenStore.get(email);
    }

    public String getEmailByToken(String token) {
        for (Map.Entry<String, String> entry : tokenStore.entrySet()) {
            if (entry.getValue().equals(token)) {
                return entry.getKey(); // the email
            }
        }
        return null;
    }

}
