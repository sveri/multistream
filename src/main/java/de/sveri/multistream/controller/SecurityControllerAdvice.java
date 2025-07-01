package de.sveri.multistream.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SecurityControllerAdvice {

	@ModelAttribute("currentUser")
	public Map<String, Object> getCurrentUser(OAuth2AuthenticationToken authentication) {
		if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
			OAuth2User oauth2User = authentication.getPrincipal();
			Map<String, Object> userMap = new HashMap<>();
			userMap.put("username", oauth2User.getAttribute("login"));
			userMap.put("displayName", oauth2User.getAttribute("display_name"));
			return userMap;
		}
		return null;
	}

	@ModelAttribute("isAuthenticated")
	public boolean isAuthenticated(OAuth2AuthenticationToken authentication) {
		return authentication != null && authentication.getPrincipal() instanceof OAuth2User;
	}
}