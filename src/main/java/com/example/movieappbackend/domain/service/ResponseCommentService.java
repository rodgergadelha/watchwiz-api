package com.example.movieappbackend.domain.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.example.movieappbackend.domain.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ResponseCommentService {

	private final ResponseCommentRepository repository;

	private final ResponseCommentMapper mapper;
	
	public Page<ResponseCommentDto> findAllByPostComment(String postCommentUuid, Pageable pageable) {
		PostComment postComment = null;
		return repository.findAllByPostComment(postComment, pageable)
				.map((responseComment) -> mapper.entityToDto(responseComment));
	}
	
	public ResponseCommentDto findByUuid(String responseCommentUuid) {
		ResponseComment responseComment = repository.findByUuid(responseCommentUuid)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Cannot find response comment with uuid: %s", responseCommentUuid)
				));
		return mapper.entityToDto(responseComment);
	}
	
	@Transactional
	public void save(ResponseCommentForm form) {
		ResponseComment responseComment = mapper.formToEntity(form);
		User user = null;
		PostComment postComment = null;
		responseComment.setPostComment(postComment);
		responseComment.setUser(user);
		repository.save(responseComment);
	}
	
	@Transactional
	public void updateText(String responseCommentUuid, String text) {
		ResponseComment responseComment = null;
		responseComment.setText(text);
	}
	
	@Transactional
	public void remove(String responseCommentUuid) {
		findByUuid(responseCommentUuid);
		try {
			repository.deleteByUuid(responseCommentUuid);
			repository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(e.getMessage());
		}
	}
}
