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
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.service.AuthService;
import com.example.movieappbackend.domain.service.UserService;
import com.example.movieappbackend.domain.service.WatchedService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

	private final UserService service;
	
	@GetMapping
	public ResponseEntity<List<UserDto>> findAllUsers() {
		List<UserDto> userDtos = service.findAllUsers();
		return ResponseEntity.ok(userDtos);
	}
	
	@GetMapping("/{username}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "username", paramType = "path", dataType = "String",
				   required = true),
	})
	public ResponseEntity<UserDto> findByUsername(@PathVariable String username) {
		UserDto userDto = service.findUserDtoByUsername(username);
		return ResponseEntity.ok(userDto);
	}
	
	@DeleteMapping("/my-account")
	public ResponseEntity<?> deleteAuthenticatedUser() {
		service.removeAuthenticatedUser();
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/my-account/watched-movies")
	public ResponseEntity<List<String>> watchedMovies() {
		return ResponseEntity.ok(service.watchedMovies());
	}
}
