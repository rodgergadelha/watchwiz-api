package com.example.movieappbackend.domain.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
	private final UserRepository userRepository;
	
	private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
    	User user = userRepository.findByUsername(username)
		.orElseThrow(() -> new EntityNotFoundException(String.format("Cannot find user with username: %s.", username)));
    	
    	return new org.springframework.security
                .core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(),
                		true, true, true, getAuthorities("USER"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
