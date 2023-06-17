package com.example.movieappbackend.domain.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.dto.PostCommentDto;
import com.example.movieappbackend.api.dtos.dto.UserDto;
import com.example.movieappbackend.api.dtos.form.PostCommentForm;
import com.example.movieappbackend.api.mapper.PostCommentMapper;
import com.example.movieappbackend.api.mapper.UserMapper;
import com.example.movieappbackend.domain.exception.EntityInUseException;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.Post;
import com.example.movieappbackend.domain.model.PostComment;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.repository.PostCommentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostCommentService {

	private final PostCommentRepository repository;
	
	private final PostService postService;
	
	private final PostCommentMapper mapper;
	
	private final UserService userService;
	
	private final UserMapper userMapper;
	
	public Page<PostCommentDto> findAllByPost(String postUuid, Pageable pageable) {
		Post post = postService.findByUuid(postUuid);
		return repository.findAllByPost(post, pageable)
				.map((postComment) -> mapper.entityToDto(postComment));
	}
	
	public PostComment findByUuid(String uuid) {
		return repository.findByUuid(uuid)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Cannot find post comment with uuid: %s", uuid)
				));
	}
	
	public PostCommentDto findPostCommentDtoByUuid(String uuid) {
		PostComment postComment = findByUuid(uuid);
		return mapper.entityToDto(postComment);
	}
	
	@Transactional
	public PostCommentDto save(String postUuid, PostCommentForm form) {
		PostComment postComment = mapper.formToEntity(form);
		User user = userService.getAuthenticatedUser();
		Post post = postService.findByUuid(postUuid);
		postComment.setPost(post);
		postComment.setUser(user);
		postComment = repository.save(postComment);
		return mapper.entityToDto(postComment);
	}
	
	@Transactional
	public PostCommentDto updateText(String uuid, String text) {
		PostComment postComment = findByUuid(uuid);
		User user = postComment.getUser();
		userService.checkIfLogged(user);
		postComment.setText(text);
		return mapper.entityToDto(postComment);
	}
	
	@Transactional
	public void remove(String uuid) {
		PostComment postComment = findByUuid(uuid);
		User user = postComment.getUser();
		userService.checkIfLogged(user);
		try {
			repository.delete(postComment);
			repository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(e.getMessage());
		}
	}
	
	public Page<UserDto> usersThatLikedPostComment(String postCommentUuid, Pageable pageable) {
		PostComment postComment = findByUuid(postCommentUuid);
		List<User> usersThatLiked = postComment.getUsersThatLiked();
		int size = usersThatLiked.size();
		Page<User> usersThatLikedPage = new PageImpl<User>(usersThatLiked, pageable, size);
		return usersThatLikedPage.map(user -> userMapper.entityToDto(user));
	}
	
	@Transactional
	public void likePostComment(String postCommentUuid) {
		PostComment postComment = findByUuid(postCommentUuid);
		User loggedInUser = userService.getAuthenticatedUser();
		List<User> usersThatLiked = postComment.getUsersThatLiked();
		if(!usersThatLiked.contains(loggedInUser)) usersThatLiked.add(loggedInUser);
	}
	
	@Transactional
	public void dislikePostComment(String postCommentUuid) {
		PostComment postComment = findByUuid(postCommentUuid);
		User loggedInUser = userService.getAuthenticatedUser();
		postComment.getUsersThatLiked().remove(loggedInUser);
	}
}
