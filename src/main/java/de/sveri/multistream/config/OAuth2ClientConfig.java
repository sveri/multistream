package de.sveri.multistream.config;

//@Configuration
//public class OAuth2ClientConfig {
//
//	@Bean
//	public OAuth2AuthorizedClientService oAuth2AuthorizedClientService(OAuth2AuthorizedClientRepository repository,
//			ClientRegistrationRepository clientRegistrationRepository) {
//
//		return new DatabaseOAuth2AuthorizedClientService(repository, clientRegistrationRepository);
//	}
//
//	@Bean
//	public OAuth2AuthorizedClientManager authorizedClientManager(
//			ClientRegistrationRepository clientRegistrationRepository,
//			OAuth2AuthorizedClientService authorizedClientService) {
//
//		OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
//				.authorizationCode().refreshToken().build();
//
//		DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
//				clientRegistrationRepository,
//				(org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository) authorizedClientService);
//
//		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//		return authorizedClientManager;
//	}
//}
