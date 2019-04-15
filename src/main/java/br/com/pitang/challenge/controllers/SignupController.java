package br.com.pitang.challenge.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pitang.challenge.common.Constants;
import br.com.pitang.challenge.common.Response;
import br.com.pitang.challenge.common.Validator;
import br.com.pitang.challenge.models.Phone;
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
		Response responseMessage = new Response();
		try {
			/*Checks if all relevant fiels are valids*/
			if(!validateUser(user)) {
				responseMessage.setCode(0);
				responseMessage.setMessage(Constants.MESSAGE_INVALID_FIELDS);
				response.addHeader("Message",responseMessage.GetResponseMessage());
				throw new Exception();
			}
			
			LocalDate created = LocalDate.now();
			user.setCreated_at(created);
			userRepository.save(user);
			jwtTokenProvider.createToken(user.getEmail(), user.getPassword(), response);
			response.addHeader(Constants.AUTHORIZATION, token);			
		}catch (ConstraintViolationException e) {
			responseMessage.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			responseMessage.setMessage(Constants.MESSAGE_EMAIL_EXISTS);
			String errorMessage = responseMessage.GetResponseMessage();
			response.addHeader("Message", errorMessage);						
		} 		
	}

	/**
	 * Verify if all user's fields are valid.
	 * @param user
	 * @return
	 */
	private boolean validateUser(User user) {
		boolean isValid = (Validator.getInstance().isEmailValid(user.getEmail())&&
		Validator.getInstance().isOnlyLetters(user.getFirstName())&&
		Validator.getInstance().isOnlyLetters(user.getLastName())&&
		validatePhone(user.getPhones()));
		return isValid;
	}
	
	/**
	 * Verify if all phone's field are valid.
	 * @param phones
	 * @return
	 */
	private boolean validatePhone(List<Phone> phones) {
		boolean isPhoneValide = false;
		if(phones != null) {
			for(Phone phone : phones) {
				String countryCode = phone.getCountry_code().substring(0, 1);
				isPhoneValide = (Validator.getInstance().isOnlyNumber(phone.getCountry_code())&&
								Validator.getInstance().isOnlyNumber(phone.getNumber().toString())&&
								Validator.getInstance().isOnlyNumber(countryCode));
			}			
		}
		return isPhoneValide;
	}
	
	@GetMapping("/status/check")
	public String status() {
		return "Laufend";
	}	
}
