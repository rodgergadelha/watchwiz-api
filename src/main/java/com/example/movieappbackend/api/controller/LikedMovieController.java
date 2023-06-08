package com.example.movieappbackend.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieappbackend.domain.service.LikedMovieService;
import com.example.movieappbackend.domain.service.UserMovieListServiceAbstract;

import lombok.AllArgsConstructor;


@CrossOrigin
@RestController
@RequestMapping("/users/my-account/liked-movies")
@AllArgsConstructor
public class LikedMovieController extends UserMovieListControllerAbstract {
	
	private final LikedMovieService service;
	
	protected UserMovieListServiceAbstract getService() {
		return this.service;
	}
}
