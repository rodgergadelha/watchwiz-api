package com.example.movieappbackend.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			.authorizeRequests()
			.antMatchers("/auth/**").permitAll()
			.antMatchers(HttpMethod.GET, "/genres/**").permitAll()
            .antMatchers(HttpMethod.GET, "/services/**").permitAll()
            .antMatchers("/v2/api-docs",
                    "/configuration/ui",
                    "/swagger-resources/**",
                    "/configuration/security",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/webjars/**")
            .permitAll()
			.anyRequest().authenticated()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.oauth2ResourceServer().opaqueToken();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
}
