package com.gamboom.eventium.controller;

import com.gamboom.eventium.config.GitHubUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private OAuth2AuthorizedClientService clientService;

    @Mock
    private OAuth2User principal;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2AuthorizedClient client;

    @Mock
    private HttpServletResponse response;

    @Mock
    private GitHubUserService gitHubUserService; // ✅ Ensure this mock is added

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(loginController, "gitHubUserService", gitHubUserService); // ✅ Inject mock manually
    }

    @Test
    @DisplayName("Redirect to app with OAuth2 user and token")
    void redirectToApp_WithValidUserAndToken_ShouldRedirectToDeepLink() throws IOException {
        when(principal.getAttribute("email")).thenReturn("johndoe@example.com");
        when(authentication.getName()).thenReturn("mock-authentication-name");
        when(clientService.loadAuthorizedClient("github", authentication.getName())).thenReturn(client);
        when(client.getAccessToken()).thenReturn(new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "mock-token", null, null));

        loginController.redirectToApp(principal, authentication, response);

        verify(gitHubUserService, times(1)).saveToken("johndoe@example.com", "mock-token");
        verify(response, times(1)).sendRedirect("eventium://auth?email=johndoe@example.com&token=mock-token");
    }

    @Test
    @DisplayName("Redirect to app - Missing email should redirect to login error")
    void redirectToApp_WithoutEmail_ShouldRedirectToLoginError() throws IOException {
        when(principal.getAttribute("email")).thenReturn(null);
        when(authentication.getName()).thenReturn("mock-authentication-name");
        when(clientService.loadAuthorizedClient("github", authentication.getName())).thenReturn(client);
        when(client.getAccessToken()).thenReturn(new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "mock-token", null, null));

        loginController.redirectToApp(principal, authentication, response);

        verify(response, times(1)).sendRedirect("/login?error=true");
    }

    @Test
    @DisplayName("Redirect to app - Missing token should redirect to login error")
    void redirectToApp_WithoutToken_ShouldRedirectToLoginError() throws IOException {
        when(principal.getAttribute("email")).thenReturn("johndoe@example.com");
        when(authentication.getName()).thenReturn("mock-authentication-name");
        when(clientService.loadAuthorizedClient("github", authentication.getName())).thenReturn(null); // No token

        loginController.redirectToApp(principal, authentication, response);

        verify(response, times(1)).sendRedirect("/login?error=true");
    }

    @Test
    @DisplayName("Redirect to app - Missing principal should redirect to login")
    void redirectToApp_WithoutPrincipal_ShouldRedirectToLogin() throws IOException {
        loginController.redirectToApp(null, authentication, response);

        verify(response, times(1)).sendRedirect("/login");
    }

    @Test
    @DisplayName("Redirect to app - Missing authentication should redirect to login")
    void redirectToApp_WithoutAuthentication_ShouldRedirectToLogin() throws IOException {
        loginController.redirectToApp(principal, null, response);

        verify(response, times(1)).sendRedirect("/login");
    }

}
