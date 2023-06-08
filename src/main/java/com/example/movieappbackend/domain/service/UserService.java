package com.example.movieappbackend.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.dto.MovieDto;
import com.example.movieappbackend.api.dtos.dto.UserDto;
import com.example.movieappbackend.api.dtos.form.RegisterForm;
import com.example.movieappbackend.api.mapper.MovieMapper;
import com.example.movieappbackend.api.mapper.UserMapper;
import com.example.movieappbackend.domain.exception.BusinessException;
import com.example.movieappbackend.domain.exception.EntityInUseException;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.MovieListItem;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.repository.UserRepository;
import com.example.movieappbackend.domain.repository.VerificationTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository repository;
	
	private final VerificationTokenRepository verificationTokenRepository;
	
	private final UserMapper mapper;
	
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
			verificationTokenRepository.deleteByUser(user);
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
	
	public List<MovieListItem> watchedMovies() {
		return getAuthenticatedUser().getWatched();
	}
	
	@Transactional
	public void saveWatchedMovie(MovieListItem movie) {
		User user = getAuthenticatedUser();
		user.getWatched().add(movie);
	}
}
