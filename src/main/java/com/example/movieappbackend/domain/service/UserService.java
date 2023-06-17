package com.example.movieappbackend.domain.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.dto.UserDto;
import com.example.movieappbackend.api.dtos.form.RegisterForm;
import com.example.movieappbackend.api.mapper.UserMapper;
import com.example.movieappbackend.domain.exception.BusinessException;
import com.example.movieappbackend.domain.exception.EntityInUseException;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.NotificationEmail;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.model.VerificationToken;
import com.example.movieappbackend.domain.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository repository;
	
	private final UserMapper mapper;
	
	private final PasswordEncoder passwordEncoder;
	
	private final MailService mailService;
	
	private final VerificationTokenService verificationTokenService;
	
	public List<UserDto> findAllUsers() {
		return repository.findAll().stream().map(user -> mapper.entityToDto(user))
				.collect(Collectors.toList());
	}	
	
	public UserDto findUserDtoByUsername(String username) {
		User user = findByUsername(username);
		return mapper.entityToDto(user);
	}
	
	public User findByUsername(String username) {
		return repository.findByUsername(username).orElseThrow(
				() -> new EntityNotFoundException(
						String.format("Cannot find user with username: %s", username)));
	}
	
	@Transactional
	public User save(RegisterForm form) {
		if(repository.existsByUsername(form.getUsername())) {
			throw new BusinessException(String.format("Username: %s already exists", form.getUsername()));
		}
		User user = mapper.formToEntity(form);
		return repository.save(user);
	}
	
	@Transactional
	public void remove(User user) {
		try {
			repository.delete(user);
			repository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(e.getMessage());
		}
	}
	
	@Transactional
	public void remove(String username) {
		User user = findByUsername(username);
		remove(user);
	}
	
	public User getAuthenticatedUser() {
		BearerTokenAuthentication authentication = (BearerTokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
		String username = (String) authentication.getTokenAttributes().get("user_name");
		User user = findByUsername(username);
		return user;
	}
	
	@Transactional
	public void removeAuthenticatedUser() {
		remove(getAuthenticatedUser());
	}
	
	@Transactional
	public UserDto updateAuthenticatedUser(RegisterForm form) {
		User user = getAuthenticatedUser();
		String email = form.getEmail();
		String username = form.getUsername();
		String password = form.getPassword();
		Date birthdate = form.getBirthdate();
		String profileImagePath = form.getProfileImagePath();
		
		if(!email.equals(user.getEmail())) {
			String token = verificationTokenService.generateVerificationToken(user);
			String emailBody = String.format("Click on the below url to update your email:\n"
					+ "http://localhost:8080/users/my-account/email-update/%s?email=%s", token, email);
			mailService.sendMail(new NotificationEmail(
					"Confirm your email update!", email, emailBody
			));
		}
		
		if(!username.equals(user.getUsername())) {
			if(repository.existsByUsername(form.getUsername())) {
				throw new BusinessException(String.format("Username: %s already exists", form.getUsername()));
			}
			user.setUsername(username);
		}
		
		if(!passwordEncoder.encode(password).equals(user.getPassword())) {
			user.setPassword(passwordEncoder.encode(form.getPassword()));
		}
		
		if(!birthdate.equals(user.getBirthdate())) {
			user.setBirthdate(birthdate);
		}
		
		if(profileImagePath == null || !profileImagePath.equals(user.getProfileImagePath())) {
			user.setProfileImagePath(profileImagePath);
		}
		
		return mapper.entityToDto(user);
	}
	
	@Transactional
	public void verifyTokenAndUpdateUserEmail(String token, String email) {
		VerificationToken verificationToken = verificationTokenService.findByToken(token);
		User user = verificationToken.getUser();
		user.setEmail(email);
		verificationTokenService.remove(verificationToken);
	}
	
	public void checkIfLogged(User user) {
		User loggedInUser = getAuthenticatedUser();
		if(!loggedInUser.equals(user)) throw new BusinessException("Operation not allowed");
	}
}
