package com.example.movieappbackend.domain.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

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
	
	private final String UPLOAD_DIRECTORY = "file:src/main/resources/images/";
	
	@Value("${api.host}")
	private String host;
	
	public UserDto authenticatedUser() {
		return mapper.entityToDto(getAuthenticatedUser());
	}
	
	public List<UserDto> findAllUsers() {
		return repository.findAll().stream().map(user -> mapper.entityToDto(user))
				.collect(Collectors.toList());
	}
	
	public Page<UserDto> following(Pageable pageable) {
		List<User> following = getAuthenticatedUser().getFollowing();
		int size = following.size();
		return new PageImpl<User>(following, pageable, size).map(user -> mapper.entityToDto(user));
	}
	
	public Page<UserDto> followers(Pageable pageable) {
		return repository.findFollowers(getAuthenticatedUser(), pageable)
				.map(user -> mapper.entityToDto(user));
	}
	
	@Transactional
	public void follow(String username) {
		User loggedInUser = getAuthenticatedUser();
		if(loggedInUser.getUsername().equals(username)) throw new BusinessException("Cannot follow yourself");
		User userToFollow = findByUsername(username);
		if(!userToFollow.isEnabled()) throw new BusinessException("Cannot follow a disabled user");
		List<User> following = loggedInUser.getFollowing();
		if(!following.contains(userToFollow)) following.add(userToFollow);
	}
	
	@Transactional
	public void unfollow(String username) {
		User loggedInUser = getAuthenticatedUser();
		User userToUnfollow = findByUsername(username);
		loggedInUser.getFollowing().remove(userToUnfollow);
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
		} else if(repository.existsByEmail(form.getEmail())) {
			throw new BusinessException(String.format("Email: %s already exists", form.getEmail()));
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
	public String updateAuthenticatedUser(RegisterForm form) {
		User user = getAuthenticatedUser();
		String email = form.getEmail();
		String username = form.getUsername();
		String password = form.getPassword();
		LocalDate birthdate = form.getBirthdate();
		MultipartFile image = form.getImage();
		boolean changedEmail = !email.equals(user.getEmail());
		
		if(changedEmail) {
			String token = verificationTokenService.generateVerificationToken(user);
			String emailBody = String.format("Click on the below url to update your email:\n"
					+ "%s/users/my-account/email-update/%s?email=%s", host, token, email);
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
		
		if(birthdate.compareTo(user.getBirthdate()) != 0) {
			user.setBirthdate(birthdate);
		}
		
		if(image != null) {
			uploadProfileImage(username, image);
		}
		
		return changedEmail ? "Check your email to complete the email update" : null;
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
	
	public void uploadProfileImage(String imageName, MultipartFile image) {
		if(image != null && !image.isEmpty()) {
			try {
				Path path = ResourceUtils.getFile(UPLOAD_DIRECTORY + imageName)
						.getAbsoluteFile().toPath();
				Files.write(path, image.getBytes());
			} catch (IOException e) {
				throw new RuntimeException("Error at image uploading");
			}
		}
	}
	
	public Resource getProfileImage() {
		User user = getAuthenticatedUser();
		String imageName = user.getUsername();
		ByteArrayResource resource;
		try {
			Path path = ResourceUtils.getFile(UPLOAD_DIRECTORY + imageName)
					.getAbsoluteFile().toPath();
			resource = new ByteArrayResource(Files.readAllBytes(path));
		} catch (NoSuchFileException e) {
			return null;
		} catch (IOException e) {
			throw new RuntimeException("Error at image download");
		}
		return resource;
	}
}
