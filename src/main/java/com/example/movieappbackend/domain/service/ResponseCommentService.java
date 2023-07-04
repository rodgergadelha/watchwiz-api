package com.example.movieappbackend.domain.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.dto.ResponseCommentDto;
import com.example.movieappbackend.api.dtos.dto.UserDto;
import com.example.movieappbackend.api.dtos.form.ResponseCommentForm;
import com.example.movieappbackend.api.mapper.ResponseCommentMapper;
import com.example.movieappbackend.api.mapper.UserMapper;
import com.example.movieappbackend.domain.exception.EntityInUseException;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.PostComment;
import com.example.movieappbackend.domain.model.ResponseComment;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.repository.ResponseCommentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ResponseCommentService {

	private final ResponseCommentRepository repository;

	private final UserService userService;
	
	private final PostCommentService postCommentService;
	
	private final ResponseCommentMapper mapper;
	
	private final UserMapper userMapper;
	
	public Page<ResponseCommentDto> findAllByPostComment(String postCommentUuid, Pageable pageable) {
		PostComment postComment = postCommentService.findByUuid(postCommentUuid);
		return repository.findAllByPostComment(postComment, pageable)
				.map((responseComment) -> {
					ResponseCommentDto responseCommentDto = mapper.entityToDto(responseComment);
					responseCommentDto.setUser(userMapper.entityToDto(responseComment.getUser()));
					return responseCommentDto;
				});
	}
	
	public ResponseComment findByUuid(String uuid) {
		return repository.findByUuid(uuid)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Cannot find response comment with uuid: %s", uuid)
				));
	}
	
	public ResponseCommentDto findResponseComnentDtoByUuid(String uuid) {
		ResponseComment responseComment = findByUuid(uuid);
		ResponseCommentDto responseCommentDto = mapper.entityToDto(responseComment);
		responseCommentDto.setUser(userMapper.entityToDto(responseComment.getUser()));
		return responseCommentDto;
	}
	
	@Transactional
	public ResponseCommentDto save(String postCommentUuid, ResponseCommentForm form) {
		ResponseComment responseComment = mapper.formToEntity(form);
		User user = userService.getAuthenticatedUser();
		PostComment postComment = postCommentService.findByUuid(postCommentUuid);
		responseComment.setPostComment(postComment);
		responseComment.setUser(user);
		responseComment = repository.save(responseComment);
		ResponseCommentDto responseCommentDto = mapper.entityToDto(responseComment);
		responseCommentDto.setUser(userMapper.entityToDto(user));
		return responseCommentDto;
	}
	
	@Transactional
	public ResponseCommentDto updateText(String uuid, String text) {
		ResponseComment responseComment = findByUuid(uuid);
		User user = responseComment.getUser();
		userService.checkIfLogged(user);
		responseComment.setText(text);
		ResponseCommentDto responseCommentDto = mapper.entityToDto(responseComment);
		responseCommentDto.setUser(userMapper.entityToDto(user));
		return responseCommentDto;
	}
	
	@Transactional
	public void remove(String uuid) {
		ResponseComment responseComment = findByUuid(uuid);
		User user = responseComment.getUser();
		userService.checkIfLogged(user);
		try {
			repository.delete(responseComment);
			repository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(e.getMessage());
		}
	}
	
	public Page<UserDto> usersThatLikedResponseComment(String responseCommentUuid, Pageable pageable) {
		ResponseComment responseComment = findByUuid(responseCommentUuid);
		List<User> usersThatLiked = responseComment.getUsersThatLiked();
		int size = usersThatLiked.size();
		Page<User> usersThatLikedPage = new PageImpl<User>(usersThatLiked, pageable, size);
		return usersThatLikedPage.map(user -> userMapper.entityToDto(user));
	}
	
	@Transactional
	public void likeResponseComment(String responseCommentUuid) {
		ResponseComment responseComment = findByUuid(responseCommentUuid);
		User loggedInUser = userService.getAuthenticatedUser();
		List<User> usersThatLiked = responseComment.getUsersThatLiked();
		if(!usersThatLiked.contains(loggedInUser)) usersThatLiked.add(loggedInUser);
	}
	
	@Transactional
	public void dislikeResponseComment(String responseCommentUuid) {
		ResponseComment responseComment = findByUuid(responseCommentUuid);
		User loggedInUser = userService.getAuthenticatedUser();
		responseComment.getUsersThatLiked().remove(loggedInUser);
	}
}
