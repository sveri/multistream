package de.sveri.multistream.service;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class YoutubeApiService {

    public void updateYouTubeStreamTitle(String accessToken, String broadcastId, String newTitle) {
    WebClient webClient = WebClient.builder()
        .baseUrl("https://www.googleapis.com/youtube/v3")
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        .build();

    Map<String, Object> body = Map.of(
        "id", broadcastId,
        "snippet", Map.of("title", newTitle)
    );

    webClient.patch()
        .uri(uriBuilder -> uriBuilder
            .path("/liveBroadcasts")
            .queryParam("part", "snippet")
            .build())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .bodyToMono(String.class)
        .block();
}
    
}
