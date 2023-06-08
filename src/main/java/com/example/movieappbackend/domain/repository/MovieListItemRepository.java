package com.example.movieappbackend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.movieappbackend.domain.model.MovieListItem;

@Repository
public interface MovieListItemRepository extends JpaRepository<MovieListItem, String>{

}
