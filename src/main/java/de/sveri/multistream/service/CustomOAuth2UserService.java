package de.sveri.multistream.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(userRequest);

		// Process Twitch user data here
		String twitchId = oauth2User.getAttribute("id");
		String username = oauth2User.getAttribute("login");
		String email = oauth2User.getAttribute("email");

		// Save to database or perform other logic

		System.out.println(oauth2User.getAttribute("email").toString());

		return oauth2User;
	}
}
