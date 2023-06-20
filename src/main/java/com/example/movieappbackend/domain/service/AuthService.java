package com.example.movieappbackend.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.movieappbackend.api.dtos.form.RegisterForm;
import com.example.movieappbackend.domain.model.NotificationEmail;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.model.VerificationToken;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	
	private final UserService userService;
	
	private final VerificationTokenService verificationTokenService;
	
	private final MailService mailService;

	@Transactional
	public void signup(RegisterForm form) {
		form.setPassword(passwordEncoder.encode(form.getPassword()));
		User user = userService.save(form);
		String token = verificationTokenService.generateVerificationToken(user);
		String emailBody = String.format("Thank you for signing up to WatchWiz, click on the below url to activate your account:\n"
				+ "http://localhost:8080/auth/account-verification/%s", token);
		mailService.sendMail(new NotificationEmail(
				"Please activate your account!", user.getEmail(), emailBody
		));
		MultipartFile image = form.getImage();
		userService.uploadProfileImage(form.getUsername(), image);
	}
	
	@Transactional
	public void verifyAccount(String token) {
		VerificationToken verificationToken = verificationTokenService.findByToken(token);
		User user = verificationToken.getUser();
		user.setEnabled(true);
		verificationTokenService.remove(verificationToken);
	}
}
