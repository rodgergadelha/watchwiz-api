package com.example.movieappbackend.api.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.movieappbackend.api.dtos.dto.PostCommentDto;
import com.example.movieappbackend.api.dtos.form.PostCommentForm;
import com.example.movieappbackend.domain.model.PostComment;

@Component
public class PostCommentMapper {

	private final ModelMapper modelMapper;
	
	public PostCommentMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	public PostComment formToEntity(PostCommentForm form) {
		return modelMapper.map(form, PostComment.class);
	}
	
	public PostCommentDto entityToDto(PostComment postComment) {
		PostCommentDto postCommentDto = modelMapper.map(postComment, PostCommentDto.class);
		postCommentDto.setPostUuid(postComment.getPost().getUuid());
		return postCommentDto;
	}
	
	public void copyFormDataToEntity(PostCommentForm form, PostComment postComment) {
		modelMapper.map(form, postComment);
	}
}
