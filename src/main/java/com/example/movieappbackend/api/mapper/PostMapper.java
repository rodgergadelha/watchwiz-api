package com.example.movieappbackend.api.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.movieappbackend.api.dtos.dto.PostDto;
import com.example.movieappbackend.api.dtos.form.PostForm;
import com.example.movieappbackend.domain.model.Post;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PostMapper {

	private final ModelMapper modelMapper;
	
	public Post formToEntity(PostForm form) {
		return modelMapper.map(form, Post.class);
	}
	
	public PostDto entityToDto(Post post) {
		PostDto postDto = modelMapper.map(post, PostDto.class);
		postDto.setUsername(post.getWatchedMovie().getUserMoviePair().getUser().getUsername());
		postDto.setUserProfileImagePath(post.getWatchedMovie().getUserMoviePair().getUser().getProfileImagePath());
		postDto.setMovieTitle(post.getWatchedMovie().getUserMoviePair().getMovie().getTitle());
		postDto.setMovieRate(post.getWatchedMovie().getRate());
		postDto.setMoviePosterUrl(post.getWatchedMovie().getUserMoviePair().getMovie().getPosterUrl());
		return postDto;
	}
	
	public void copyFormDataToEntity(PostForm form, Post post) {
		modelMapper.map(form, post);
	}
}
