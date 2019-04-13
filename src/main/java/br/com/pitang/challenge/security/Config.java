package br.com.pitang.challenge.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.bind.annotation.PostMapping;

@Configuration
@EnableWebSecurity
public class Config extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	public Config(JwtTokenProvider jwtTokenProv) {
		this.jwtTokenProvider = jwtTokenProv;
	}
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
    //@formatter:off
    http.headers().frameOptions().disable()
    	.and()
        .csrf().disable()        
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/console/**").permitAll()            
            .antMatchers("/challenge/signup").permitAll()
            .antMatchers("/challenge/signin").permitAll()
            .antMatchers(HttpMethod.GET, "/challenge/status/check").permitAll()            
            .anyRequest().authenticated()
            
        .and()
        .apply(new JwtConfigurer(jwtTokenProvider));
    }
	
}
