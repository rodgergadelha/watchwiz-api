package com.example.movieappbackend.domain.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.dto.PostCommentDto;
import com.example.movieappbackend.api.dtos.form.PostCommentForm;
import com.example.movieappbackend.api.mapper.PostCommentMapper;
import com.example.movieappbackend.domain.exception.EntityInUseException;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.Post;
import com.example.movieappbackend.domain.model.PostComment;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.repository.PostCommentRepository;
import com.example.movieappbackend.domain.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostCommentService {

	private final PostCommentRepository repository;
	
	private final PostService postService;
	
	private final PostCommentMapper mapper;
	
	private final UserService userService;
	
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
	public void save(PostCommentForm form) {
		PostComment postComment = mapper.formToEntity(form);
		User user = userService.getAuthenticatedUser();
		Post post = postService.findByUuid(form.getPostUuid());
		postComment.setPost(post);
		postComment.setUser(user);
		repository.save(postComment);
	}
	
	@Transactional
	public void updateText(String uuid, String text) {
		PostComment postComment = findByUuid(uuid);
		postComment.setText(text);
	}
	
	@Transactional
	public void remove(String uuid) {
		PostComment postComment = findByUuid(uuid);
		try {
			repository.delete(postComment);
			repository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(e.getMessage());
		}
	}
}
