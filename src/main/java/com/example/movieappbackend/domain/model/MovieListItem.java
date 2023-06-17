package com.example.movieappbackend.domain.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode
@Data
@Entity
public class MovieListItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
    @Id
	private String imdbId;
	
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String overview;
	
	private String posterUrl;
	
	private int imdbRating;
	
	private List<String> genres;
}
