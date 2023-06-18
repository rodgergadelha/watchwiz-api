package com.example.movieappbackend.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.movieappbackend.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
	
	void deleteByUsername(String username);
	
	boolean existsByUsername(String username);
	
	@Query("from User u where :user member of u.following")
	Page<User> findFollowers(@Param("user") User user, Pageable pageable);
}
