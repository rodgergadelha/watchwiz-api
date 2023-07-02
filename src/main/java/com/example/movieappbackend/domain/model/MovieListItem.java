package com.example.movieappbackend.domain.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

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
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "movie_list_item_genre",
    joinColumns = @JoinColumn(name = "movie_list_item_imdb_id"),
    inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private List<Genre> genres;
}
