package br.com.pitang.challenge.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pitang.challenge.common.Hashing;
import br.com.pitang.challenge.models.User;
import br.com.pitang.challenge.repository.PhoneRepository;
import br.com.pitang.challenge.repository.UserRepository;
import br.com.pitang.challenge.security.JwtTokenProvider;

@RestController
@RequestMapping(value = "/challenge")
public class SignupController {	
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@GetMapping("/user")
	public List<User>listUsers(){
		return (List<User>) userRepository.findAll();
	}
	
	@PostMapping("/signup")
	public String createUser(@RequestBody User user) throws Exception{
		userRepository.save(user);
		String token = jwtTokenProvider.createToken(user.getEmail(), user.getPassword());
		return token;
	}
	
	@GetMapping("/status/check")
	public String status() {
		return "Laufend";
	}	
}
