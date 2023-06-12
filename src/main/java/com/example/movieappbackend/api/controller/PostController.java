package com.example.movieappbackend.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieappbackend.api.dtos.dto.PostDto;
import com.example.movieappbackend.api.dtos.form.PostForm;
import com.example.movieappbackend.domain.service.PostService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
public class PostController {

	private final PostService service;
	
	@GetMapping("/posts")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", paramType = "query", dataType = "integer",
		   required = true, value = "number of the page"),

		@ApiImplicitParam(name = "size", paramType = "query", dataType = "integer",
		required = true, value = "size of a page"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<Page<List<PostDto>>> findAllPosts(@RequestParam("page") int page,
															@RequestParam("size") int size) {
		Pageable pageable = PageRequest.of(page, size);
		List<PostDto> postsDtos = service.findAllPosts();
		Page<List<PostDto>> postsDtosPages = new PageImpl(postsDtos, pageable, postsDtos.size());
		return ResponseEntity.ok(postsDtosPages);
	}
	
	@GetMapping("/{username}/posts")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "username", paramType = "path", dataType = "String",
				   required = true),
		
		@ApiImplicitParam(name = "page", paramType = "query", dataType = "integer",
		   required = true, value = "number of the page"),

		@ApiImplicitParam(name = "size", paramType = "query", dataType = "integer",
		required = true, value = "size of a page"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<Page<List<PostDto>>> findAllByUser(@PathVariable String username,
															@RequestParam("page") int page,
															@RequestParam("size") int size) {
		Pageable pageable = PageRequest.of(page, size);
		List<PostDto> postsDtos = service.findAllByUser(username);
		Page<List<PostDto>> postsDtosPages = new PageImpl(postsDtos, pageable, postsDtos.size());
		return ResponseEntity.ok(postsDtosPages);
	}
	
	@PostMapping("/posts")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<PostDto> save(@Valid @RequestBody PostForm form) {
		PostDto postDto = service.save(form);
		return ResponseEntity.ok(postDto);
	}
	
	@DeleteMapping("/posts/{postUuid}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "postUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of post to delete"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<?> deletePost(@PathVariable String postUuid) {
		service.remove(postUuid);
		return ResponseEntity.ok().build();
	}
}
