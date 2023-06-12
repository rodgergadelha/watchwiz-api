package com.example.movieappbackend.api.dtos.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PostForm {

	@NotBlank
	private String url;
	
	@Size(max = 33000)
	@NotNull
    private String text;
	
	@NotBlank
	private String watchedMovie;
}
