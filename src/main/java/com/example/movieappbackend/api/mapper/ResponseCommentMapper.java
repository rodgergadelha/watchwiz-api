package com.example.movieappbackend.api.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.movieappbackend.api.dtos.dto.ResponseCommentDto;
import com.example.movieappbackend.api.dtos.form.ResponseCommentForm;
import com.example.movieappbackend.domain.model.ResponseComment;

@Component
public class ResponseCommentMapper {

	private final ModelMapper modelMapper;
	
	public ResponseCommentMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	public ResponseComment formToEntity(ResponseCommentForm form) {
		return modelMapper.map(form, ResponseComment.class);
	}
	
	public ResponseCommentDto entityToDto(ResponseComment responseComment) {
		return modelMapper.map(responseComment, ResponseCommentDto.class);
	}
	
	public void copyFormDataToEntity(ResponseCommentForm form, ResponseComment responseComment) {
		modelMapper.map(form, responseComment);
	}
}
