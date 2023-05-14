package com.example.movieappbackend.api.dtos.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostCommentForm {

	@Size(max = 33000)
	@NotNull
	private String text;
	
	@NotBlank
	private String userUuid;
	
	@NotBlank
	private String postUuid;
}
