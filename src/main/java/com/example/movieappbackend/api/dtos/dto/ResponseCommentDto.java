package com.example.movieappbackend.api.dtos.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseCommentDto {

	private String uuid;
	private String text;
	private String userUuid;
	private String postCommentUuid;
}
