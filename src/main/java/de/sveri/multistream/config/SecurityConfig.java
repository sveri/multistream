package de.sveri.multistream.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${spring.security.oauth2.client.registration.twitch.client-id}")
	private String twitchClientId;

	@Autowired
	private TwitchOAuth2UserService twitchOAuth2UserService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				authz -> authz.requestMatchers("/", "/login", "/error").permitAll().anyRequest().authenticated())
//				.oauth2Login(oauth2 -> oauth2.loginPage("/login").defaultSuccessUrl("/dashboard", true)
//						.userInfoEndpoint(userInfo -> userInfo.userService(userService)))
				.oauth2Login(oauth2 -> oauth2.loginPage("/login").defaultSuccessUrl("/dashboard", true)
						.userInfoEndpoint(userInfo -> userInfo.userService(twitchOAuth2UserService)))
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/logout-success")
						.invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID"));

		return http.build();
	}
}