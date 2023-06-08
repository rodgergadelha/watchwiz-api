package com.example.movieappbackend.domain.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.model.VerificationToken;
import com.example.movieappbackend.domain.repository.VerificationTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VerificationTokenService {

	private final VerificationTokenRepository repository;
	
	public VerificationToken findByToken(String token) {
		return repository.findByToken(token).orElseThrow(
				() -> new EntityNotFoundException("Invalid token.")
				);
	}
	
	public void save(VerificationToken verificationToken) {
		repository.save(verificationToken);
	}
	
	public void remove(VerificationToken verificationToken) {
		repository.delete(verificationToken);
	}
	
	public void removeByUser(User user) {
		repository.deleteByUser(user);
	}
	
	@Transactional
	protected String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setUser(user);
		verificationToken.setToken(token);
		save(verificationToken);
		return token;
	}
}
