package de.sveri.multistream.entities;

public record UpdateChannelRequest(String gameId, String title) {
}
//public record UpdateChannelRequest(String gameId, String broadcasterLanguage, String title, int delay, String[] tags,
//		String[] contentClassificationLabels, Boolean isBrandedContent) {
//}
