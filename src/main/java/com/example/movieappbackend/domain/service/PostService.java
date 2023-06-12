package com.example.movieappbackend.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.dto.PostDto;
import com.example.movieappbackend.api.dtos.form.PostForm;
import com.example.movieappbackend.api.mapper.PostMapper;
import com.example.movieappbackend.domain.exception.EntityInUseException;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.Post;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.model.WatchedMovie;
import com.example.movieappbackend.domain.repository.PostRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {

	private final PostRepository repository;
	
	private final WatchedMovieService watchedMovieService;
	
	private final UserService userService;
	
	private final PostMapper mapper;
	
	public List<PostDto> findAllPosts() {
		return repository.findAll().stream()
				.map(post -> mapper.entityToDto(post))
				.collect(Collectors.toList());
	}
	
	@Transactional
	public PostDto save(PostForm form) {
		Post post = mapper.formToEntity(form);
		User loggedInUser = userService.getAuthenticatedUser();
		WatchedMovie watchedMovie = watchedMovieService.findByImdbIdAndUser(form.getWatchedMovie(), loggedInUser);
		post.setWatchedMovie(watchedMovie);
		post = repository.save(post);
		return mapper.entityToDto(post);
	}
	
	public List<PostDto> findAllByUser(String username) {
		User user = userService.findByUsername(username);
		return repository.findAllByUser(user).stream()
				.map(post -> mapper.entityToDto(post))
				.collect(Collectors.toList());
	}
	
	public Post findByUuid(String uuid) {
		return repository.findByUuid(uuid).orElseThrow(
				() -> new EntityNotFoundException(String.format("Cannot find post with uuid: %s", uuid))
		);
	}
	
	@Transactional
	public void remove(String postUuid) {
		Post post = findByUuid(postUuid);
		try {
			repository.delete(post);
			repository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(e.getMessage());
		}
	}
}
