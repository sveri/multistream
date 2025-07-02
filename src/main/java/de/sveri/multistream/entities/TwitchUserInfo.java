package de.sveri.multistream.entities;

public record TwitchUserInfo(String id, String login, String displayName, String type, String broadcasterType,
		String description, String profileImageUrl, String offlineImageUrl, long viewCount, String email,
		String createdAt) {
}
