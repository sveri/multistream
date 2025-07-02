package de.sveri.multistream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

	@Autowired
	private OAuth2AuthorizedClientService clientService;

	@GetMapping("/login")
	public String login(OAuth2AuthenticationToken authentication) {
		return "login";
	}

	@GetMapping("/oauth/success")
	public String oauthSuccess(Authentication authentication, HttpServletRequest request) {
		// Get the authorized client
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
		OAuth2AuthorizedClient client = clientService
				.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());

		// Extract tokens
		OAuth2AccessToken accessToken = client.getAccessToken();
		OAuth2RefreshToken refreshToken = client.getRefreshToken();

		// Store tokens (you might want to save these to database)
		String accessTokenValue = accessToken.getTokenValue();
		String refreshTokenValue = refreshToken != null ? refreshToken.getTokenValue() : null;

		System.out.println("Access Token: " + accessTokenValue);
		System.out.println("Refresh Token: " + refreshTokenValue);
		System.out.println("Token Expires At: " + accessToken.getExpiresAt());

		// Redirect to your application
		return "redirect:/dashboard";
	}

	@GetMapping("/logout-success")
	public String logoutSuccess() {
		return "logout-success";
	}

	@PostMapping("/custom-logout")
	public String customLogout(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {

		new SecurityContextLogoutHandler().logout(request, response, authentication);

		return "redirect:/logout-success";
	}

}
