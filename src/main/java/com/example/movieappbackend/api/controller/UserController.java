package com.example.movieappbackend.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieappbackend.api.dtos.dto.UserDto;
import com.example.movieappbackend.api.dtos.form.RegisterForm;
import com.example.movieappbackend.domain.service.UserService;

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
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<UserDto> findByUsername(@PathVariable String username) {
		UserDto userDto = service.findUserDtoByUsername(username);
		return ResponseEntity.ok(userDto);
	}
	
	@DeleteMapping("/my-account")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<?> deleteAuthenticatedUser() {
		service.removeAuthenticatedUser();
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/my-account")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<UserDto> updateAuthenticatedUser(@RequestBody @Valid RegisterForm form) {
		UserDto userDto = service.updateAuthenticatedUser(form);
		return ResponseEntity.ok(userDto);
	}
	
	@GetMapping("/my-account/email-update/{token}")
	public ResponseEntity<String> updateAuthenticatedUserEmail(
			@PathVariable String token,
			@RequestParam("email") String email) {
		
		service.verifyTokenAndUpdateUserEmail(token, email);
		return ResponseEntity.ok("Email updated successfully!");
	}
}
