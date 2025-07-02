package de.sveri.multistream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		return "dashboard";
	}

	@PostMapping("/update/title")
	public String updateTitle(@RequestParam("title") String title, Authentication authentication,
			RedirectAttributes redirectAttributes) {
		channelService.updateChannelTitle(authentication.getName(), title);
		redirectAttributes.addFlashAttribute("updatedTitle", title);

		return "redirect:/dashboard"; // or return to a success page
	}

}
