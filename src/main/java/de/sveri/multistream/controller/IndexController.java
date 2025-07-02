package de.sveri.multistream.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("/")
	public String getMethodName(Model model) {
		model.addAttribute("name", "Sveri");
		return "index";
	}

	@GetMapping("/error")
	public String error() {
		return "error";
	}

}
