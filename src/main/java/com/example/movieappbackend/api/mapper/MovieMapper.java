package com.example.movieappbackend.api.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.movieappbackend.api.dtos.dto.MovieDto;
import com.example.movieappbackend.api.dtos.form.MovieListItemForm;
import com.example.movieappbackend.domain.model.MovieListItem;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MovieMapper {

	private final ModelMapper modelMapper;
	
	public MovieDto dto(Object movie) {
		return modelMapper.map(movie, MovieDto.class);
	}
	
	public MovieListItem movieListItem(MovieDto movieDto) {
		MovieListItem movieListItem = modelMapper.map(movieDto, MovieListItem.class);
		movieListItem.setPosterUrl(movieDto.getPosterURLs().get("original"));
		return movieListItem;
	}
	
	public MovieListItem movieListItem(MovieListItemForm form) {
		return modelMapper.map(form, MovieListItem.class);
	}
}
