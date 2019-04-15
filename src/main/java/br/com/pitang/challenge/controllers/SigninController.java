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

import br.com.pitang.challenge.common.Constants;
import br.com.pitang.challenge.common.Hashing;
import br.com.pitang.challenge.common.Response;
import br.com.pitang.challenge.common.Validator;
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
	public void signin(@RequestBody Credentials credential, HttpServletResponse response) throws NoSuchAlgorithmException {
		 String email = credential.getEmail();
		 String password = Hashing.getInstance().getHash(credential.getPassword());
		 Response customResponse = new Response();		
		 if(!Validator.getInstance().isFilledField(email)
				 || !Validator.getInstance().isFilledField(password)) {
			 customResponse.setCode(0);
			 customResponse.setMessage(Constants.MESSAGE_MISSING_FIELDS);
		 }
	
		try {	 
			 User user = this.userRepository.findByCredential(email, password);
			 if(user != null) {
				 jwtTokenProvider.createToken(user.getEmail(),user.getPassword(), response); 
				 LocalDate lastLogin= LocalDate.now();
				 user.setLast_login(lastLogin);
				 this.userRepository.save(user);				 
			 }else {
				 customResponse.setCode(0);
				 customResponse.setMessage(Constants.MESSAGE_INVALID_MAIL_PASSWORD);
			 }
	     } catch (Exception e) {
	    	 customResponse.setCode(HttpStatus.FORBIDDEN.value());
	    	 customResponse.setMessage(Constants.MESSAGE_INVALID_MAIL_PASSWORD);
	     }
	}
}
