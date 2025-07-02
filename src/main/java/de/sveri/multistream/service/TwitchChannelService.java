package de.sveri.multistream.service;

import java.time.Instant;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import de.sveri.multistream.db.DatabaseOAuth2AuthorizedClientService;
import de.sveri.multistream.entities.TwitchChannelInfo;
import de.sveri.multistream.entities.TwitchUserInfo;

@Service
public class TwitchChannelService {

	private final TwitchApiService twitchApiService;
	private final DatabaseOAuth2AuthorizedClientService clientService;

	public TwitchChannelService(TwitchApiService twitchApiService,
			DatabaseOAuth2AuthorizedClientService clientService) {
		this.twitchApiService = twitchApiService;
		this.clientService = clientService;
	}

	public void updateChannelTitle(String principalName, String newTitle) {
		String accessToken = getAccessToken(principalName);
		TwitchUserInfo user = twitchApiService.getCurrentUser(accessToken);
		twitchApiService.updateChannelTitle(accessToken, user.id(), newTitle);
	}

	public TwitchChannelInfo getChannelInfo(String principalName) {
		String accessToken = getAccessToken(principalName);
		TwitchUserInfo user = twitchApiService.getCurrentUser(accessToken);
		return twitchApiService.getChannelInfo(accessToken, user.id());
	}

//	public void updateChannelTitleAndGame(String principalName, String newTitle, String gameId) {
//		String accessToken = getAccessToken(principalName);
//		TwitchUserInfo user = twitchApiService.getCurrentUser(accessToken);
//		twitchApiService.updateChannelTitleAndGame(accessToken, user.id(), newTitle, gameId);
//	}

	private String getAccessToken(String principalName) {
		OAuth2AuthorizedClient client = clientService.loadAuthorizedClient("twitch", principalName);
		if (client == null) {
			throw new RuntimeException("No Twitch authorization found for user: " + principalName);
		}

		OAuth2AccessToken accessToken = client.getAccessToken();
		if (accessToken.getExpiresAt() != null && accessToken.getExpiresAt().isBefore(Instant.now())) {
			throw new RuntimeException("Access token has expired");
		}

		return accessToken.getTokenValue();
	}
}
