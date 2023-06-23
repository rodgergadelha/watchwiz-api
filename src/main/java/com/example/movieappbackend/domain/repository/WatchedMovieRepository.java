package com.example.movieappbackend.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.model.UserMoviePair;
import com.example.movieappbackend.domain.model.WatchedMovie;


@Repository
public interface WatchedMovieRepository extends JpaRepository<WatchedMovie, UserMoviePair>{

	@Query("from WatchedMovie w where w.userMoviePair.user = :user")
	List<WatchedMovie> findAllWatchedMoviesByUser(@Param("user") User user);
	
	@Query("select count(w) > 0 from WatchedMovie w where w.userMoviePair.movie.imdbId = :imdbId and w.userMoviePair.user = :user")
	boolean existsByImdbIdAndUser(@Param("imdbId") String imdbId, @Param("user") User user);
	
	@Query("from WatchedMovie w where w.userMoviePair.movie.imdbId = :imdbId and w.userMoviePair.user = :user")
	Optional<WatchedMovie> findByImdbIdAndUser(@Param("imdbId") String imdbId, @Param("user") User user);
}