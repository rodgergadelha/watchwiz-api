package com.example.movieappbackend.domain.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.dto.PostCommentDto;
import com.example.movieappbackend.api.dtos.form.PostCommentForm;
import com.example.movieappbackend.api.mapper.PostCommentMapper;
import com.example.movieappbackend.domain.entity.Post;
import com.example.movieappbackend.domain.entity.PostComment;
import com.example.movieappbackend.domain.entity.User;
import com.example.movieappbackend.domain.exception.EntityInUseException;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.repository.PostCommentRepository;

@Service
public class PostCommentService {

	private final PostCommentRepository repository;
	
	private final PostCommentMapper mapper;
	
	public PostCommentService(PostCommentRepository repository, PostCommentMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}
	
	public Page<PostCommentDto> findAllByPost(String postUuid, Pageable pageable) {
		Post post = null;
		return repository.findAllByPost(post, pageable)
				.map((postComment) -> mapper.entityToDto(postComment));
	}
	
	public PostCommentDto findAndValidateByUuid(String uuid) {
		PostComment postComment = repository.findByUuid(uuid)
				.orElseThrow(() -> new EntityNotFoundException());
		return mapper.entityToDto(postComment);
	}
	
	@Transactional
	public void save(PostCommentForm form) {
		PostComment postComment = mapper.formToEntity(form);
		User user = null;
		Post post = null;
		postComment.setPost(post);
		postComment.setUser(user);
		repository.save(postComment);
	}
	
	@Transactional
	public void updateText(String postCommentUuid, String text) {
		PostComment postComment = null;
		postComment.setText(text);
	}
	
	@Transactional
	public void remove(String postCommentUuid) {
		findAndValidateByUuid(postCommentUuid);
		try {
			repository.deleteByUuid(postCommentUuid);
			repository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(e.getMessage());
		}
	}
	
	/*
	public int getLikesFromPostComment(String postCommentUuid) {
		PostComment postComment = null;
		return repository.getTotalLikesFromPostComment(postComment);
	}
	
	public List<ResponseCommentDto> getResponseComments(String postCommentUuid) {
		PostComment postComment = null;
		return repository.
	}
	*/
}
