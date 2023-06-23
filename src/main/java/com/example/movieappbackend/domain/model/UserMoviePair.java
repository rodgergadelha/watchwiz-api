package com.example.movieappbackend.domain.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Embeddable
@Data
@EqualsAndHashCode
public class UserMoviePair implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user")
	@EqualsAndHashCode.Include
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "id_movie_list_item")
	@EqualsAndHashCode.Include
	private MovieListItem movie;
}
