package com.example.movieappbackend.api.dtos.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostCommentForm {

	@Size(max = 33000)
	@NotBlank
	private String text;
}
