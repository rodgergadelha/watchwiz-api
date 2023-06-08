package com.example.movieappbackend.domain.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.movieappbackend.api.dtos.form.RegisterForm;
import com.example.movieappbackend.domain.exception.BusinessException;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.NotificationEmail;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.model.VerificationToken;
import com.example.movieappbackend.domain.repository.VerificationTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	
	private final UserService userService;
	
	private final VerificationTokenRepository verificationTokenRepository;
	
	private final MailService mailService;

	@Transactional
	public void signup(RegisterForm form) {
		form.setPassword(passwordEncoder.encode(form.getPassword()));
		User user = userService.save(form);
		String token = generateVerificationToken(user);
		
		mailService.sendMail(new NotificationEmail(
				"Please activate your account!",
				user.getEmail(),
				"Thank you for signing up to WatchWiz, click on the below url to activate your account:\n"
				+ "http://localhost:8080/auth/account-verification/" + token
		));
	}
	
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public User getAuthenticatedUser() {
		UserDetails userDetails = (UserDetails)(getAuthentication().getPrincipal());
		User user = userService.findByUsername(userDetails.getUsername());
		return user;
	}
	
	@Transactional
	public void verifyAccount(String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(
			() -> new EntityNotFoundException("Invalid token.")
		);
		User user = verificationToken.getUser();
		if(user.isEnabled()) throw new BusinessException("User already enabled.");
		user.setEnabled(true);
	}
	
	@Transactional
	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setUser(user);
		verificationToken.setToken(token);
		verificationTokenRepository.save(verificationToken);
		return token;
	}
}
