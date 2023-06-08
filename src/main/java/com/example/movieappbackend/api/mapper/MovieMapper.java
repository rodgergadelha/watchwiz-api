package com.example.movieappbackend.api.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.movieappbackend.api.dtos.dto.MovieDto;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MovieMapper {

	private final ModelMapper modelMapper;
	
	public MovieDto dto(Object movie) {
		return modelMapper.map(movie, MovieDto.class);
	}
}
