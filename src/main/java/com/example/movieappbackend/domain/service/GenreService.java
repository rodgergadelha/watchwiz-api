package com.example.movieappbackend.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.movieappbackend.domain.exception.BusinessException;
import com.example.movieappbackend.domain.model.Genre;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.repository.GenreRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GenreService {

	private final GenreRepository repository;
	
	private final UserService userService;
	
	public List<Genre> findAll() {
		return repository.findAll();
	}
	
	public Genre save(String name) {
		User loggedInUser = userService.getAuthenticatedUser();
		if(!loggedInUser.getUsername().equals("rodger")) throw new BusinessException("Only admin can register genres");
		Genre genre = new Genre();
		genre.setName(name);
		return repository.save(genre);
	}
	
	public List<Genre> findAllByIdIn(List<Long> ids) {
		return repository.findAllByIdIn(ids);
	}
}
