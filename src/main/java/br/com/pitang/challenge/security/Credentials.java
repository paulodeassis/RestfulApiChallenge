package br.com.pitang.challenge.security;

import java.io.Serializable;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Credential Class to reuse the relevants attributes from 
 * User Entity to provide the authentication.
 * @author Paulo de Assis
 *
 */
public class Credentials  extends UsernamePasswordAuthenticationFilter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private String password;
	
	public String getPassword() {
		return password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/*public Credentials(String email, String password) {		
		this.email = email;
		this.password = password;
	}	*/

	
	public String getEmail() {
		return email;
	}

}
