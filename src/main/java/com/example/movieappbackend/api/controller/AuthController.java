package com.example.movieappbackend.api.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieappbackend.api.dtos.form.RegisterForm;
import com.example.movieappbackend.domain.service.AuthService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

	private final AuthService service;
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody @Valid RegisterForm form) {
		service.signup(form);
		return ResponseEntity.ok("User registration successful!");
	}
	
	@GetMapping("/account-verification/{token}")
	public ResponseEntity<String> verifyAccount(@PathVariable String token) {
		service.verifyAccount(token);
		return ResponseEntity.ok("Account activated successfully.");
	}
}
