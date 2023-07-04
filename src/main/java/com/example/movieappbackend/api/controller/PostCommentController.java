package com.example.movieappbackend.api.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieappbackend.api.dtos.dto.PostCommentDto;
import com.example.movieappbackend.api.dtos.dto.UserDto;
import com.example.movieappbackend.api.dtos.form.PostCommentForm;
import com.example.movieappbackend.domain.service.PostCommentService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
public class PostCommentController {

	private final PostCommentService service;
	
	@GetMapping("/posts/{postUuid}/comments")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "postUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of the post"),
		
		@ApiImplicitParam(name = "page", paramType = "query", dataType = "integer",
		   required = true, value = "number of the page"),

		@ApiImplicitParam(name = "size", paramType = "query", dataType = "integer",
		required = true, value = "size of a page"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<Page<PostCommentDto>> findAllPostCommentsByPost( @PathVariable String postUuid,
														@RequestParam("page") int page,
														@RequestParam("size") int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<PostCommentDto> postCommentDtos = service.findAllByPost(postUuid, pageable);
		return postCommentDtos.isEmpty() ? ResponseEntity.noContent().build() :
			ResponseEntity.ok(postCommentDtos);
	}
	
	@GetMapping("/posts/comments/{postCommentUuid}/likes")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "postCommentUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of post comment"),
		
		@ApiImplicitParam(name = "page", paramType = "query", dataType = "integer",
		   required = true, value = "number of the page"),

		@ApiImplicitParam(name = "size", paramType = "query", dataType = "integer",
		required = true, value = "size of a page"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<Page<UserDto>> likes( @PathVariable String postCommentUuid,
												@RequestParam("page") int page,
												@RequestParam("size") int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<UserDto> userDtos = service.usersThatLikedPostComment(postCommentUuid, pageable);
		return ResponseEntity.ok(userDtos);
	}
	
	@PostMapping("/posts/comments/{postCommentUuid}/likes")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "postCommentUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of post comment"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<?> likePostComment(@PathVariable String postCommentUuid) {
		service.likePostComment(postCommentUuid);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/posts/comments/{postCommentUuid}/likes")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "postCommentUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of post comment"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<?> dislikePostComment(@PathVariable String postCommentUuid) {
		service.dislikePostComment(postCommentUuid);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/posts/comments/{postCommentUuid}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "postCommentUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of the post comment")
	})
	public ResponseEntity<PostCommentDto> findByUuid(@PathVariable String postCommentUuid) {
		PostCommentDto postCommentDto = service.findPostCommentDtoByUuid(postCommentUuid);
		return ResponseEntity.ok(postCommentDto);
	}
	
	@PostMapping("/posts/{postUuid}/comments")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "postUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of the post"),
		
		@ApiImplicitParam(name = "postUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of the post")
	})
	public ResponseEntity<PostCommentDto> save( @PathVariable String postUuid,
												@Valid @RequestBody PostCommentForm form) {
		PostCommentDto postCommentDto = service.save(postUuid, form);
		return ResponseEntity.status(HttpStatus.CREATED).body(postCommentDto);
	}
	
	@PatchMapping("/posts/comments/{postCommentUuid}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "postCommentUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of the post comment")
	})
	public ResponseEntity<PostCommentDto> updateText(@PathVariable String postCommentUuid,
													@RequestParam("text") String text) {
		PostCommentDto postCommentDto = service.updateText(postCommentUuid, text);
		return ResponseEntity.ok(postCommentDto);
	}
	
	@DeleteMapping("/posts/comments/{postCommentUuid}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "postCommentUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of the post comment")
	})
	public ResponseEntity<?> remove(@PathVariable String postCommentUuid) {
		service.remove(postCommentUuid);
		return ResponseEntity.ok().build();
	}
}
