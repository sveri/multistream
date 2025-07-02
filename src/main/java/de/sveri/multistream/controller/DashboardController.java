package de.sveri.multistream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.sveri.multistream.service.TwitchChannelService;
import de.sveri.multistream.service.TwitchTokenService;

@Controller
public class DashboardController {

	@Autowired
	private TwitchTokenService tokenService;

	@Autowired
	private TwitchChannelService channelService;

	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
		OAuth2User user = token.getPrincipal();

		model.addAttribute("username", user.getAttribute("display_name"));

		System.out.println("token from service: " + tokenService.getAccessToken(authentication.getName()));

		return "dashboard";
	}

	@PostMapping("/update/title")
	public String updateTitle(@RequestParam("title") String title, Model model, Authentication authentication) {
		// Process the title
		System.out.println("Received title: " + title);
		System.out.println("current channelinfo: " + channelService.getChannelInfo(authentication.getName()));
		channelService.updateChannelTitle(authentication.getName(), title);

		model.addAttribute("message", "Title updated successfully to " + title);
		return "redirect:/dashboard"; // or return to a success page
	}

}
