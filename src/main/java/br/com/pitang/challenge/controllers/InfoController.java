package br.com.pitang.challenge.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/challenge")
public class InfoController {
	
	@GetMapping("/info")
	public String status() {
		String info = "It works!\n\n"
				+ "Maybe you will need to know some informations like:\n"
				+ "Database: H2\n"
				+ "Console (if access granted):h2-console\n"
				+ "default Instance:mem:test\n"
				+ "default user:sa\n"
				+ "default password:\n"
				+ "Server:Heroku";
		return info;
	}

}
