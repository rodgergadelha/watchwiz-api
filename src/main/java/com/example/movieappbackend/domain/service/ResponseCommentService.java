package com.example.movieappbackend.domain.service;

import org.springframework.stereotype.Service;

import com.example.movieappbackend.domain.repository.ResponseCommentRepository;

@Service
public class ResponseCommentService {

	private final ResponseCommentRepository repository;
	
	public ResponseCommentService(ResponseCommentRepository repository) {
		this.repository = repository;
	}
}
