package de.sveri.multistream.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

@Service
public class TwitchTokenService {

	@Autowired
	private OAuth2AuthorizedClientService clientService;

	public String getAccessToken(String principalName) {
		OAuth2AuthorizedClient client = clientService.loadAuthorizedClient("twitch", principalName);
		return client != null ? client.getAccessToken().getTokenValue() : null;
	}

	public String getRefreshToken(String principalName) {
		OAuth2AuthorizedClient client = clientService.loadAuthorizedClient("twitch", principalName);
		OAuth2RefreshToken refreshToken = client != null ? client.getRefreshToken() : null;
		return refreshToken != null ? refreshToken.getTokenValue() : null;
	}

	public boolean isTokenExpired(String principalName) {
		OAuth2AuthorizedClient client = clientService.loadAuthorizedClient("twitch", principalName);
		if (client == null) {
			return true;
		}

		Instant expiresAt = client.getAccessToken().getExpiresAt();
		return expiresAt != null && expiresAt.isBefore(Instant.now());
	}
}
