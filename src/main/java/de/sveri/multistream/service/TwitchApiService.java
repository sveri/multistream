package de.sveri.multistream.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import de.sveri.multistream.entities.TwitchChannelInfo;
import de.sveri.multistream.entities.TwitchChannelResponse;
import de.sveri.multistream.entities.TwitchUserInfo;
import de.sveri.multistream.entities.TwitchUserResponse;
import de.sveri.multistream.entities.UpdateChannelRequest;

@Service
public class TwitchApiService {

	private static final String TWITCH_API_BASE_URL = "https://api.twitch.tv/helix";
	private final WebClient webClient;
	private final String clientId;

	public TwitchApiService(@Value("${twitch.client-id}") String clientId) {
		this.clientId = clientId;
		this.webClient = WebClient.builder().baseUrl(TWITCH_API_BASE_URL).build();
	}

	public void updateChannelTitle(String accessToken, String broadcasterId, String title) {
		UpdateChannelRequest request = new UpdateChannelRequest(null, title);
		try {
			webClient.patch().uri("/channels?broadcaster_id=" + broadcasterId)
					.header("Authorization", "Bearer " + accessToken).header("Client-Id", clientId)
					.contentType(MediaType.APPLICATION_JSON).bodyValue(request).retrieve().toBodilessEntity().block();

		} catch (Exception e) {
			throw new RuntimeException("Failed to update channel: " + e.getMessage(), e);
		}
	}

	public TwitchUserInfo getCurrentUser(String accessToken) {
		try {
			TwitchUserResponse response = webClient.get().uri("/users").header("Authorization", "Bearer " + accessToken)
					.header("Client-Id", clientId).retrieve().bodyToMono(TwitchUserResponse.class).block();

			if (response != null && response.data().length > 0) {
				return response.data()[0];
			}

			throw new RuntimeException("No user data found");

		} catch (Exception e) {
			throw new RuntimeException("Failed to get current user: " + e.getMessage(), e);
		}
	}

	public TwitchChannelInfo getChannelInfo(String accessToken, String broadcasterId) {
		try {
			TwitchChannelResponse response = webClient.get().uri("/channels?broadcaster_id=" + broadcasterId)
					.header("Authorization", "Bearer " + accessToken).header("Client-Id", clientId).retrieve()
					.bodyToMono(TwitchChannelResponse.class).block();

			if (response != null && response.data().length > 0) {
				return response.data()[0];
			}

			throw new RuntimeException("No channel data found");

		} catch (Exception e) {
			throw new RuntimeException("Failed to get channel info: " + e.getMessage(), e);
		}
	}
}
