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

import com.example.movieappbackend.api.dtos.dto.ResponseCommentDto;
import com.example.movieappbackend.api.dtos.dto.UserDto;
import com.example.movieappbackend.api.dtos.form.ResponseCommentForm;
import com.example.movieappbackend.domain.service.ResponseCommentService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
public class ResponseCommentController {
	
	private final ResponseCommentService service;
	
	@GetMapping("/posts/comments/{postCommentUuid}/responses")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "postCommentUuid", paramType = "path", dataType = "string",
		   required = true, value = "UUID of the post comment"),
		
		@ApiImplicitParam(name = "page", paramType = "query", dataType = "integer",
		   required = true, value = "number of the page"),

		@ApiImplicitParam(name = "size", paramType = "query", dataType = "integer",
		required = true, value = "size of a page"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<?> findAllResponseCommentsByPostComment(  @PathVariable String postCommentUuid,
																	@RequestParam("page") int page,
																	@RequestParam("size") int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<ResponseCommentDto> postCommentDtos = service.findAllByPostComment(postCommentUuid, pageable);
		return postCommentDtos.isEmpty() ? ResponseEntity.noContent().build() :
			ResponseEntity.ok(postCommentDtos);
	}
	
	@GetMapping("/posts/comments/responses/{responseCommentUuid}/likes")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "responseCommentUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of response comment"),
		
		@ApiImplicitParam(name = "page", paramType = "query", dataType = "integer",
		   required = true, value = "number of the page"),

		@ApiImplicitParam(name = "size", paramType = "query", dataType = "integer",
		required = true, value = "size of a page"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<Page<UserDto>> likes( @PathVariable String responseCommentUuid,
												@RequestParam("page") int page,
												@RequestParam("size") int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<UserDto> userDtos = service.usersThatLikedResponseComment(responseCommentUuid, pageable);
		return ResponseEntity.ok(userDtos);
	}
	
	@PostMapping("/posts/comments/responses/{responseCommentUuid}/likes")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "responseCommentUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of response comment"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<?> likeResponseComment(@PathVariable String responseCommentUuid) {
		service.likeResponseComment(responseCommentUuid);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/posts/comments/responses/{responseCommentUuid}/likes")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "responseCommentUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of response comment"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<?> dislikeResponseComment(@PathVariable String responseCommentUuid) {
		service.dislikeResponseComment(responseCommentUuid);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/posts/comments/responses/{responseCommentUuid}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "responseCommentUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of the response comment")
	})
	public ResponseEntity<ResponseCommentDto> findByUuid(@PathVariable String responseCommentUuid) {
		ResponseCommentDto responseCommentDto = service.findResponseComnentDtoByUuid(responseCommentUuid);
		return ResponseEntity.ok(responseCommentDto);
	}
	
	@PostMapping("/posts/comments/{postCommentUuid}/responses")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "postCommentUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of the post comment"),
		
		@ApiImplicitParam(name = "postUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of the post")
	})
	public ResponseEntity<ResponseCommentDto> save( @PathVariable String postCommentUuid,
												@Valid @RequestBody ResponseCommentForm form) {
		ResponseCommentDto responseCommentDto = service.save(postCommentUuid, form);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseCommentDto);
	}
	
	@PatchMapping("/posts/comments/responses/{responseCommentUuid}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "responseCommentUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of the response comment")
	})
	public ResponseEntity<ResponseCommentDto> updateText(@PathVariable String responseCommentUuid,
													@RequestParam("text") String text) {
		ResponseCommentDto responseCommentDto = service.updateText(responseCommentUuid, text);
		return ResponseEntity.ok(responseCommentDto);
	}
	
	@DeleteMapping("/posts/comments/responses/{responseCommentUuid}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "responseCommentUuid", paramType = "path", dataType = "string",
				   required = true, value = "UUID of the response comment")
	})
	public ResponseEntity<?> remove(@PathVariable String responseCommentUuid) {
		service.remove(responseCommentUuid);
		return ResponseEntity.ok().build();
	}
}
