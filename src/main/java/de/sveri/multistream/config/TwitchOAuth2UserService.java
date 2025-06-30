package de.sveri.multistream.config;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TwitchOAuth2UserService extends DefaultOAuth2UserService {

	@Value("${spring.security.oauth2.client.registration.twitch.client-id}")
	private String twitchClientId;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		if (!"twitch".equals(userRequest.getClientRegistration().getRegistrationId())) {
			return super.loadUser(userRequest);
		}

		// For Twitch, we need to manually make the request with Client-ID header
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(userRequest.getAccessToken().getTokenValue());
			headers.set("Client-ID", twitchClientId);

			HttpEntity<?> entity = new HttpEntity<>(headers);

			ResponseEntity<Map> response = restTemplate.exchange("https://api.twitch.tv/helix/users", HttpMethod.GET,
					entity, Map.class);

			Map<String, Object> userAttributes = (Map<String, Object>) ((List<?>) response.getBody().get("data"))
					.get(0);

			return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), userAttributes,
					"login");

		} catch (Exception e) {
			throw new OAuth2AuthenticationException("Failed to fetch user info from Twitch: " + e.getMessage());
		}
	}
}
