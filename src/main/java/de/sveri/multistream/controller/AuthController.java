package de.sveri.multistream.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

	@GetMapping("/login")
	public String login(OAuth2AuthenticationToken authentication) {
//		System.out.println(authentication.getName());
		return "login";
	}

	@GetMapping("/error")
	public String error() {
//		System.out.println(authentication.getName());
		return "error";
	}

	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
		OAuth2User user = token.getPrincipal();

		model.addAttribute("username", user.getAttribute("display_name"));

		return "dashboard";
	}

	@GetMapping("/logout-success")
	public String logoutSuccess() {
		return "logout-success";
	}

	// Optional: Custom logout handler for additional cleanup
	@PostMapping("/custom-logout")
	public String customLogout(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {

		// Perform any additional cleanup here
		// For example, revoke Twitch token, clear custom session data, etc.

		// Then perform standard logout
		new SecurityContextLogoutHandler().logout(request, response, authentication);

		return "redirect:/logout-success";
	}

}
