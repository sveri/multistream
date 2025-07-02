package de.sveri.multistream.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private TwitchOAuth2UserService twitchOAuth2UserService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authz -> authz.requestMatchers("/", "/login", "/error", "/webjars/**").permitAll()
				.anyRequest().authenticated())
				.oauth2Login(oauth2 -> oauth2.loginPage("/login").defaultSuccessUrl("/oauth/success", true)
						.userInfoEndpoint(userInfo -> userInfo.userService(twitchOAuth2UserService)))
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
						.clearAuthentication(true).deleteCookies("JSESSIONID"));

		return http.build();
	}
}