package com.example.movieappbackend.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<List<UserDto>> findAllUsers() {
		List<UserDto> userDtos = service.findAllUsers();
		return ResponseEntity.ok(userDtos);
	}
	
	@GetMapping("/my-account/following")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", paramType = "query", dataType = "integer",
				   required = true, value = "number of the page"),
		
		@ApiImplicitParam(name = "size", paramType = "query", dataType = "integer",
		   required = true, value = "size of a page"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<Page<UserDto>> following( @RequestParam("page") int page,
													@RequestParam("size") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<UserDto> userDtos = service.following(pageable);
		return userDtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(userDtos);
	}
	
	@GetMapping("/my-account/followers")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", paramType = "query", dataType = "integer",
				   required = true, value = "number of the page"),
		
		@ApiImplicitParam(name = "size", paramType = "query", dataType = "integer",
		   required = true, value = "size of a page"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<Page<UserDto>> followers( @RequestParam("page") int page,
													@RequestParam("size") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<UserDto> userDtos = service.followers(pageable);
		return userDtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(userDtos);
	}
	
	@PostMapping("/my-account/following")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "username", paramType = "query", dataType = "String",
				   required = true),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<UserDto> follow(@RequestParam("username") String username) {
		service.follow(username);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/my-account/following/{username}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "username", paramType = "path", dataType = "String",
				   required = true),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<UserDto> unfollow(@PathVariable String username) {
		service.unfollow(username);
		return ResponseEntity.ok().build();
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
	
	@PutMapping(value = "/my-account", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<UserDto> updateAuthenticatedUser(@ModelAttribute @Valid RegisterForm form) {
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
	
	@GetMapping(value = "/my-account/profile-image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> profileImage() {
		Resource resource = service.getProfileImage();
		return resource == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(resource);
	}
}
