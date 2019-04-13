package br.com.pitang.challenge.security;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import br.com.pitang.challenge.exceptions.InvalidJwtAuthenticationException;
import br.com.pitang.challenge.exceptions.UserNotFoundException;
import br.com.pitang.challenge.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
    
    public String createToken(String email, String password) {
    
    	Claims claims = Jwts.claims().setSubject(email);
        claims.put("password", password);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        
        return Jwts.builder()//
            .setClaims(claims)//
            .setIssuedAt(now)//
            .setExpiration(validity)//
            .signWith(SignatureAlgorithm.HS256, secretKey)//
            .compact();
    }
    
    public Authentication getAuthentication(String token) throws UserNotFoundException, NoSuchAlgorithmException {    	
        UserDetails userDetails =  (UserDetails) getAuthentication(getUsername(token));
        
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    
    
    public UserDetails getAuthentication(Credentials credential) throws UserNotFoundException {
    	return this.userDetailsService.loadUserByCredential(credential);
    }
    
    public Credentials getUsername(String token) throws NoSuchAlgorithmException {
    	String email = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    	String password = Jwts.parser().setSigningKey("password").parseClaimsJws(token).getBody().getSubject();
    	Credentials credential = new Credentials();
    	credential.setEmail(email);
    	credential.setPassword(password);
    	
    	return credential;//Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            return token;
        }
        return null;
    }
    
    public boolean validateToken(String token) throws InvalidJwtAuthenticationException {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
           throw new InvalidJwtAuthenticationException();//"Expired or invalid JWT token"
        }
    }
}
