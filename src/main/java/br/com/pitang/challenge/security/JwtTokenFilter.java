package br.com.pitang.challenge.security;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import br.com.pitang.challenge.exceptions.InvalidJwtAuthenticationException;
import br.com.pitang.challenge.exceptions.UserNotFoundException;
import br.com.pitang.challenge.models.User;

public class JwtTokenFilter extends GenericFilterBean {
    private JwtTokenProvider jwtTokenProvider;
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
    	HttpServletRequest  srvlRequest = (HttpServletRequest) request;
        String token = srvlRequest.getHeader("Authorization"); //jwtTokenProvider.resolveToken(srvlRequest);
        try {
			if (token != null && jwtTokenProvider.validateToken(token)) {
			    User user = token != null ? jwtTokenProvider.getAuthentication(token) : null;			  
			}
		} catch (InvalidJwtAuthenticationException | UserNotFoundException | NoSuchAlgorithmException e) {		
			e.printStackTrace();
		}
        filterChain.doFilter(request, response);
    }
}
