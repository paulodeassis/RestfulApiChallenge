package br.com.pitang.challenge.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.pitang.challenge.exceptions.UserNotFoundException;
import br.com.pitang.challenge.models.User;
import br.com.pitang.challenge.repository.UserRepository;

@Component
public class UserAuthenticationService {

	@Autowired
	UserRepository userRepository;
	
	public UserAuthenticationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public UserDetails loadUserByCredential(Credentials credential) throws UserNotFoundException {
		User user = userRepository.findByCredential(credential.getEmail(), credential.getPassword());		
		return user;
	}
	
}
