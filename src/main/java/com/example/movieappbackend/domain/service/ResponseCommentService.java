package com.example.movieappbackend.domain.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.dto.ResponseCommentDto;
import com.example.movieappbackend.api.dtos.form.ResponseCommentForm;
import com.example.movieappbackend.api.mapper.ResponseCommentMapper;
import com.example.movieappbackend.domain.exception.EntityInUseException;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.PostComment;
import com.example.movieappbackend.domain.model.ResponseComment;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.repository.ResponseCommentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ResponseCommentService {

	private final ResponseCommentRepository repository;

	private final UserService userService;
	
	private final PostCommentService postCommentService;
	
	private final ResponseCommentMapper mapper;
	
	public Page<ResponseCommentDto> findAllByPostComment(String postCommentUuid, Pageable pageable) {
		PostComment postComment = null;
		return repository.findAllByPostComment(postComment, pageable)
				.map((responseComment) -> mapper.entityToDto(responseComment));
	}
	
	public ResponseComment findByUuid(String uuid) {
		return repository.findByUuid(uuid)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Cannot find response comment with uuid: %s", uuid)
				));
	}
	
	public ResponseCommentDto findResponseComnentDtoByUuid(String uuid) {
		ResponseComment responseComment = findByUuid(uuid);
		return mapper.entityToDto(responseComment);
	}
	
	@Transactional
	public void save(ResponseCommentForm form) {
		ResponseComment responseComment = mapper.formToEntity(form);
		User user = userService.getAuthenticatedUser();
		PostComment postComment = postCommentService.findByUuid(form.getPostCommentUuid());
		responseComment.setPostComment(postComment);
		responseComment.setUser(user);
		repository.save(responseComment);
	}
	
	@Transactional
	public void updateText(String uuid, String text) {
		ResponseComment responseComment = findByUuid(uuid);
		responseComment.setText(text);
	}
	
	@Transactional
	public void remove(String uuid) {
		ResponseComment responseComment = findByUuid(uuid);
		try {
			repository.delete(responseComment);
			repository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(e.getMessage());
		}
	}
}
