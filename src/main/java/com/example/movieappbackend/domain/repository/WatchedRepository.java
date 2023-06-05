package com.example.movieappbackend.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.model.UserMoviePair;
import com.example.movieappbackend.domain.model.Watched;

public interface WatchedRepository extends JpaRepository<Watched, UserMoviePair>{
	
	@Query("select id from Watched w where w.id.user = :user")
	List<UserMoviePair> findAllByUser(@Param("user") User user);
}
