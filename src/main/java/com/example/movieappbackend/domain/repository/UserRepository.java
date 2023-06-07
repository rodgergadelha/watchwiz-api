package com.example.movieappbackend.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.model.Watched;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
	
	void deleteByUsername(String username);
	
	@Query("select w.id.user from Watched w where w.id.movieImdbId = :movie")
	List<User> findAllByWatchedMovie(@Param("movie") String movieImdbId);
	
	boolean existsByUsername(String username);
}
