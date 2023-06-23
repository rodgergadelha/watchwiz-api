package com.example.movieappbackend.api.dtos.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GenreListForm {

	@NotNull
	private List<String> genreNames;
}
