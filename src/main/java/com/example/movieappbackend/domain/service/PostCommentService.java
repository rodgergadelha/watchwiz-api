package com.example.movieappbackend.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.dto.PostCommentDto;
import com.example.movieappbackend.api.dtos.form.PostCommentForm;
import com.example.movieappbackend.api.mapper.PostCommentMapper;
import com.example.movieappbackend.domain.entity.Post;
import com.example.movieappbackend.domain.entity.PostComment;
import com.example.movieappbackend.domain.entity.User;
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
	
	public PostCommentDto findByUuid(String uuid) {
		PostComment postComment = repository.findByUuid(uuid)
				.orElseThrow(() -> new EntityNotFoundException());
		return mapper.entityToDto(postComment);
	}
	
	@Transactional
	public PostCommentDto save(PostCommentForm form) {
		PostComment postComment = mapper.formToEntity(form);
		User user = null;
		Post post = null;
		postComment.setPost(post);
		postComment.setUser(user);
		return mapper.entityToDto(postComment);
	}
	
	
	
}
