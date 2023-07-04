package com.example.movieappbackend.api.dtos.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostCommentDto {
	
	private String uuid;
	private String text;
	private UserDto user;
	private String postUuid;
}
