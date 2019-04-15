package br.com.pitang.challenge.controllers;

import java.awt.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pitang.challenge.common.Constants;
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
		String token = request.getHeader(Constants.AUTHORIZATION);
		
		if(!jwtTokenProvider.validateToken(token)) {
			response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage(Constants.MESSAGE_UNAUTHORIZED_INVALID_SESSION);
			return response.toString();
		}
		
		try {
			 user = jwtTokenProvider.getAuthentication(token);
			 user.setPassword(null);
			 if(user != null) {
				 return mapper.writeValueAsString(user);				
			 }
		} catch (UserNotFoundException e) {
			response.setCode(HttpStatus.UNAUTHORIZED.value());
			response.setMessage(Constants.MESSAGE_UNAUTHORIZED);
		}
		return response.toString();
	}	
}
