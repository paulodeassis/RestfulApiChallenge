package br.com.pitang.challenge.controllers;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pitang.challenge.common.Hashing;
import br.com.pitang.challenge.models.User;
import br.com.pitang.challenge.repository.UserRepository;
import br.com.pitang.challenge.security.Credentials;
import br.com.pitang.challenge.security.JwtTokenProvider;

@RestController
@RequestMapping(value = "/challenge")
public class AuthenticationController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/signin")
	public String signin(@RequestBody Credentials credential) throws NoSuchAlgorithmException {
		 String mail = credential.getEmail();
		 String password = Hashing.getInstance().getHash(credential.getPassword());
		 
		try {	 
			 User user = this.userRepository.findByCredential(mail, password);
	         String token = jwtTokenProvider.createToken(user.getEmail(),user.getPassword());     
	         return token;
	     } catch (Exception e) {
	    	 
	         throw new BadCredentialsException("Invalid username/password supplied");
	     }
	}
}
