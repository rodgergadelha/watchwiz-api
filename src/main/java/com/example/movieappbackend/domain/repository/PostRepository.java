package com.example.movieappbackend.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.movieappbackend.domain.model.Post;
import com.example.movieappbackend.domain.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	@Query("from Post p where p.watchedMovie.userMoviePair.user = :user")
	List<Post> findAllByUser(@Param("user") User user);
	
	Optional<Post> findByUuid(String uuid);
}
