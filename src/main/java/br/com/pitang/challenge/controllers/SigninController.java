package br.com.pitang.challenge.controllers;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pitang.challenge.common.Hashing;
import br.com.pitang.challenge.common.Response;
import br.com.pitang.challenge.models.User;
import br.com.pitang.challenge.repository.UserRepository;
import br.com.pitang.challenge.security.Credentials;
import br.com.pitang.challenge.security.JwtTokenProvider;

@RestController
@RequestMapping(value = "/challenge")
public class SigninController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/signin")
	public Response signin(@RequestBody Credentials credential, HttpServletResponse response) throws NoSuchAlgorithmException {
		 String mail = credential.getEmail();
		 String password = Hashing.getInstance().getHash(credential.getPassword());
		 Response response1 = new Response();
		try {	 
			 User user = this.userRepository.findByCredential(mail, password);
			 if(user != null) {
				 jwtTokenProvider.createToken(user.getEmail(),user.getPassword(), response); 
				 LocalDate lastLogin= LocalDate.now();
				 user.setLast_login(lastLogin);
				 this.userRepository.save(user);				 
			 }else {
				 response1.setCode(0);
				 response1.setMessage("User not found.");
			 }
	     } catch (Exception e) {
	    	 
	    	 response1.setCode(HttpStatus.FORBIDDEN.value());
	    	 response1.setMessage("Missing Fields");
	     }
		return response1;
	}
}
