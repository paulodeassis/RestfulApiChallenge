package br.com.pitang.challenge.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pitang.challenge.common.Response;
import br.com.pitang.challenge.models.User;
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
	public void createUser(@RequestBody User user, HttpServletResponse response) throws Exception{
		String token="";
		try {
			LocalDate created = LocalDate.now();
			user.setCreated_at(created);
			userRepository.save(user);
			jwtTokenProvider.createToken(user.getEmail(), user.getPassword(), response);
			response.addHeader("Authorization", token);			
		}catch (Exception e) {
			Response responseMessage = new Response();
			responseMessage.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			responseMessage.setMessage("E-mail alredy exists.");
			String errorMessage = responseMessage.GetResponseMessage();
			response.addHeader("Message", errorMessage);						
		} 		
		//return response.getHeader("Authorization");
	}	
	
	@GetMapping("/status/check")
	public String status() {
		return "Laufend";
	}	
}
