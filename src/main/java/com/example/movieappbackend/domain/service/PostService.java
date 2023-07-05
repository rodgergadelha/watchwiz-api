package com.example.movieappbackend.domain.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.dto.PostDto;
import com.example.movieappbackend.api.dtos.dto.UserDto;
import com.example.movieappbackend.api.dtos.form.PostForm;
import com.example.movieappbackend.api.mapper.PostMapper;
import com.example.movieappbackend.api.mapper.UserMapper;
import com.example.movieappbackend.domain.exception.BusinessException;
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
	
	private final UserMapper userMapper;
	
	public Page<PostDto> findAllPosts(Pageable pageable) {
		User loggedInUser = userService.getAuthenticatedUser();
		Page<Post> posts = repository.findAll(pageable);
		Page<PostDto> postsDtos = posts.map(post -> {
			boolean authenticatedUserLiked = post.getUsersThatLiked().contains(loggedInUser);
			PostDto postDto = mapper.entityToDto(post);
			postDto.setAuthenticatedUserLiked(authenticatedUserLiked);
			return postDto;
		});
		return postsDtos;
	}
	
	public PostDto findPostDtoByUuid(String uuid) {
		Post post = findByUuid(uuid);
		User loggedInUser = userService.getAuthenticatedUser();
		boolean authenticatedUserLiked = post.getUsersThatLiked().contains(loggedInUser);
		PostDto postDto = mapper.entityToDto(post);
		postDto.setAuthenticatedUserLiked(authenticatedUserLiked);
		return postDto;
	}
	
	@Transactional
	public PostDto save(PostForm form) {
		Post post = mapper.formToEntity(form);
		User loggedInUser = userService.getAuthenticatedUser();
		WatchedMovie watchedMovie = watchedMovieService.findByImdbIdAndUser(form.getWatchedMovie(), loggedInUser);
		if(repository.existsByWatchedMovie(watchedMovie)) {
			throw new BusinessException(String.format("Post with movie: %s already exists", 
					watchedMovie.getUserMoviePair().getMovie().getTitle()));
		}
		post.setWatchedMovie(watchedMovie);
		post = repository.save(post);
		return mapper.entityToDto(post);
	}
	
	public Page<PostDto> findAllByUser(String username, Pageable pageable) {
		User user = userService.findByUsername(username);
		return repository.findAllByUser(user, pageable).map(post -> mapper.entityToDto(post));
	}
	
	public Post findByUuid(String uuid) {
		return repository.findByUuid(uuid).orElseThrow(
				() -> new EntityNotFoundException(String.format("Cannot find post with uuid: %s", uuid))
		);
	}
	
	@Transactional
	public void remove(String postUuid) {
		Post post = findByUuid(postUuid);
		User user = post.getWatchedMovie().getUserMoviePair().getUser();
		userService.checkIfLogged(user);
		try {
			repository.delete(post);
			repository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(e.getMessage());
		}
	}
	
	public Page<UserDto> usersThatLikedPost(String postUuid, Pageable pageable) {
		Post post = findByUuid(postUuid);
		List<User> usersThatLiked = post.getUsersThatLiked();
		int size = usersThatLiked.size();
		Page<User> usersThatLikedPage = new PageImpl<User>(usersThatLiked, pageable, size);
		return usersThatLikedPage.map(user -> userMapper.entityToDto(user));
	}
	
	@Transactional
	public void likePost(String postUuid) {
		Post post = findByUuid(postUuid);
		User loggedInUser = userService.getAuthenticatedUser();
		List<User> usersThatLiked = post.getUsersThatLiked();
		if(!usersThatLiked.contains(loggedInUser)) usersThatLiked.add(loggedInUser);
	}
	
	@Transactional
	public void dislikePost(String postUuid) {
		Post post = findByUuid(postUuid);
		User loggedInUser = userService.getAuthenticatedUser();
		post.getUsersThatLiked().remove(loggedInUser);
	}
	
	public Page<PostDto> findAllByUserFollowing(Pageable pageable) {
		User loggedInUser = userService.getAuthenticatedUser();
		Page<Post> posts = repository.findAllByUserFollowing(loggedInUser.getFollowing(), pageable);
		Page<PostDto> postsDtos = posts.map(post -> {
			boolean authenticatedUserLiked = post.getUsersThatLiked().contains(loggedInUser);
			PostDto postDto = mapper.entityToDto(post);
			postDto.setAuthenticatedUserLiked(authenticatedUserLiked);
			return postDto;
		});
		return postsDtos;
	}
}
