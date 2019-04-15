package br.com.pitang.challenge.security;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import br.com.pitang.challenge.common.Constants;
import br.com.pitang.challenge.exceptions.InvalidJwtAuthenticationException;
import br.com.pitang.challenge.exceptions.UserNotFoundException;
import br.com.pitang.challenge.models.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	
	@Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h
    @Autowired
    private UserAuthenticationService userDetailsService;
    
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    
    public void createToken(String email, String password, HttpServletResponse response) throws UnsupportedEncodingException {
    	Date now = new Date();
    	 Date validity = new Date(now.getTime() + validityInMilliseconds);
    	 String mailPassword = email+"_"+password;
    	String token = Jwts.builder()
				.setSubject(mailPassword)
				.setExpiration(validity)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();    	
    	response.addHeader(Constants.AUTHORIZATION, Constants.TOKEN_PREFIX+ token);	
    }
    
    
    public User getAuthentication(String token) throws UserNotFoundException, NoSuchAlgorithmException {    	
        User user = getAuthentication(getUser(token));        
        return user;
    }
    
    
    public User getAuthentication(Credentials credential) throws UserNotFoundException {
    	return this.userDetailsService.loadUserByCredential(credential);
    }
    
    public Credentials getUser(String token) throws NoSuchAlgorithmException {
    	token = Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token.replace("Bearer ", ""))
					.getBody()
					.getSubject();
    	String email = token.split("_")[0];
    	String password = token.split("_")[1];
    	Credentials credential = new Credentials();
    	credential.setEmail(email);
    	credential.setPassword(password);
    	return credential;    	
    }
    
    public String resolveToken(HttpServletRequest request) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {        
    	String token = request.getHeader("Authorization");    	
        if (token != null && token.startsWith("Bearer ")) {
        	token = Jwts.parser()
        			.setSigningKey(secretKey)
        			.parseClaimsJws(token.replace("Bearer ", ""))
        			.getBody()
        			.getSubject();  
        	return token;
        }
    	
        return "Unauthorized";      
    }
    
    public boolean validateToken(String token) throws InvalidJwtAuthenticationException {
        try {
        	/*checks if the expiration date is older then today.*/
            if (Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token.replace("Bearer ", ""))
        			.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
           throw new InvalidJwtAuthenticationException();//"Expired or invalid JWT token"
        }
    }
}
