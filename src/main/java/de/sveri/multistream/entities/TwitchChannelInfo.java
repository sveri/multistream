package de.sveri.multistream.entities;

public record TwitchChannelInfo(String broadcasterId, String broadcasterLogin, String broadcasterName,
		String broadcasterLanguage, String gameId, String gameName, String title, int delay, String[] tags,
		String[] contentClassificationLabels, boolean isBrandedContent) {
}
