package de.sveri.multistream.db;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DatabaseOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

	private final OAuth2AuthorizedClientRepository repository;
	private final ClientRegistrationRepository clientRegistrationRepository;

	public DatabaseOAuth2AuthorizedClientService(OAuth2AuthorizedClientRepository repository,
			ClientRegistrationRepository clientRegistrationRepository) {
		this.repository = repository;
		this.clientRegistrationRepository = clientRegistrationRepository;
	}

	@Override
	public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId,
			String principalName) {

		Optional<OAuth2AuthorizedClientEntity> entityOpt = repository
				.findByClientRegistrationIdAndPrincipalName(clientRegistrationId, principalName);

		if (entityOpt.isEmpty()) {
			return null;
		}

		OAuth2AuthorizedClientEntity entity = entityOpt.get();
		ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(clientRegistrationId);

		if (clientRegistration == null) {
			return null;
		}

		return (T) convertToAuthorizedClient(entity, clientRegistration);
	}

	@Override
	public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {

		String clientRegistrationId = authorizedClient.getClientRegistration().getRegistrationId();
		String principalName = principal.getName();

		OAuth2AuthorizedClientEntity entity = repository
				.findByClientRegistrationIdAndPrincipalName(clientRegistrationId, principalName)
				.orElse(new OAuth2AuthorizedClientEntity(clientRegistrationId, principalName));

		// Update entity with new token information
		updateEntityFromAuthorizedClient(entity, authorizedClient);

		repository.save(entity);
	}

	@Override
	public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
		repository.deleteByClientRegistrationIdAndPrincipalName(clientRegistrationId, principalName);
	}

	// Helper method to convert entity to OAuth2AuthorizedClient
	private OAuth2AuthorizedClient convertToAuthorizedClient(OAuth2AuthorizedClientEntity entity,
			ClientRegistration clientRegistration) {

		// Parse scopes
		Set<String> scopes = null;
		if (entity.getAccessTokenScopes() != null && !entity.getAccessTokenScopes().isEmpty()) {
			scopes = Arrays.stream(entity.getAccessTokenScopes().split(",")).map(String::trim)
					.collect(Collectors.toSet());
		}

		// Create access token using constructor
		OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
				entity.getAccessTokenValue(), entity.getAccessTokenIssuedAt(), entity.getAccessTokenExpiresAt(),
				scopes);

		// Create refresh token (if present)
		OAuth2RefreshToken refreshToken = null;
		if (entity.getRefreshTokenValue() != null) {
			refreshToken = new OAuth2RefreshToken(entity.getRefreshTokenValue(), entity.getRefreshTokenIssuedAt());
		}

		return new OAuth2AuthorizedClient(clientRegistration, entity.getPrincipalName(), accessToken, refreshToken);
	}

	// Helper method to update entity from OAuth2AuthorizedClient
	private void updateEntityFromAuthorizedClient(OAuth2AuthorizedClientEntity entity,
			OAuth2AuthorizedClient authorizedClient) {

		OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

		entity.setAccessTokenType(accessToken.getTokenType().getValue());
		entity.setAccessTokenValue(accessToken.getTokenValue());
		entity.setAccessTokenIssuedAt(accessToken.getIssuedAt());
		entity.setAccessTokenExpiresAt(accessToken.getExpiresAt());

		// Store scopes as comma-separated string
		if (accessToken.getScopes() != null && !accessToken.getScopes().isEmpty()) {
			String scopes = String.join(",", accessToken.getScopes());
			entity.setAccessTokenScopes(scopes);
		}

		// Handle refresh token
		OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
		if (refreshToken != null) {
			entity.setRefreshTokenValue(refreshToken.getTokenValue());
			entity.setRefreshTokenIssuedAt(refreshToken.getIssuedAt());
		}
	}

	// Additional utility methods
	public List<OAuth2AuthorizedClientEntity> findClientsByPrincipal(String principalName) {
		return repository.findByPrincipalName(principalName);
	}

	@Scheduled(fixedRate = 3600000) // Run every hour
	public void cleanupExpiredTokens() {
		Instant oneDayAgo = Instant.now().minus(Duration.ofDays(1));
		repository.deleteExpiredTokens(oneDayAgo);
	}
}
