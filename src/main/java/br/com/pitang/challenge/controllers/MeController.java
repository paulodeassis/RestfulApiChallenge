package br.com.pitang.challenge.controllers;

import java.awt.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pitang.challenge.common.Response;
import br.com.pitang.challenge.exceptions.UserNotFoundException;
import br.com.pitang.challenge.models.User;
import br.com.pitang.challenge.repository.UserRepository;
import br.com.pitang.challenge.security.JwtTokenProvider;

@RestController
@RequestMapping(value = "/challenge")
public class MeController {	
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@GetMapping("/me")	
	public String getUser(HttpServletRequest request) throws Exception{
		Response response = new Response();
		ObjectMapper mapper = new ObjectMapper();
		User user = new User();
		String token = request.getHeader("Authorization");
		try {
			 user = jwtTokenProvider.getAuthentication(token);
			 if(user != null) {
				 
				 return mapper.writeValueAsString(user);				
				 //return mapper.writeValueAsString(user);
			 }
		} catch (UserNotFoundException e) {
			response.setCode(HttpStatus.UNAUTHORIZED.value());
			response.setMessage("Unauthorized");
		}
		return response.toString();
	}	
}
