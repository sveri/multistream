package de.sveri.multistream.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

	@GetMapping("/login")
	public String login(OAuth2AuthenticationToken authentication) {
		return "login";
	}

	@GetMapping("/oauth/success")
	public String oauthSuccess(Authentication authentication, HttpServletRequest request) {
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
