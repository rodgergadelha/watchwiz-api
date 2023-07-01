package com.example.movieappbackend.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		Genre genre = new Genre(name);
		return repository.save(genre);
	}
	
	@Transactional
	public List<Genre> save(List<String> genreNames) {
		User loggedInUser = userService.getAuthenticatedUser();
		if(!loggedInUser.getUsername().equals("rodger")) throw new BusinessException("Only admin can register genres");
		
		List<Genre> genres = genreNames.stream()
				.map(genreName -> repository.save(new Genre(genreName)))
				.collect(Collectors.toList());
		
		return genres;
	}
	
	public List<Genre> findAllByNameIn(List<String> names) {
		return repository.findAllByNameIn(names);
	}
	
	@Transactional
	public void delete(Long id) {
		repository.deleteById(id);
	}
}
