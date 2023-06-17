package com.example.movieappbackend.api.dtos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

	private String uuid;
	private String username;
	private String userProfileImagePath;
    private String text;
    private String movieTitle;
    private float movieRate;
    private String moviePosterUrl;
	private String creationDate;
}
