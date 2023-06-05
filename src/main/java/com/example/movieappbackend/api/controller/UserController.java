package com.example.movieappbackend.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieappbackend.api.dtos.dto.UserDto;
import com.example.movieappbackend.domain.service.UserService;
import com.example.movieappbackend.domain.service.WatchedService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

	private final UserService service;
	
	private final WatchedService watchedService;
	
	@GetMapping
	public ResponseEntity<List<UserDto>> findAllUsers() {
		List<UserDto> userDtos = service.findAllUsers();
		return ResponseEntity.ok(userDtos);
	}
	
	@GetMapping("/{username}/watched-movies")
	public ResponseEntity<List<String>> findUserWatchedMovies(@PathVariable String username) {
		List<String> watchedMovies = watchedService.findUserWatchedMovies(username);
		return ResponseEntity.ok(watchedMovies);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<UserDto> findByUsername(@PathVariable String username) {
		UserDto userDto = service.findUserDtoByUsername(username);
		return ResponseEntity.ok(userDto);
	}
	
	@DeleteMapping("/{username}")
	public ResponseEntity<?> removeByUsername(@PathVariable String username) {
		service.remove(username);
		return (ResponseEntity<?>) ResponseEntity.ok();
	}
}
