package com.example.movieappbackend.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.movieappbackend.domain.model.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>{

	List<Genre> findAllByNameIn(List<String> names);
}
