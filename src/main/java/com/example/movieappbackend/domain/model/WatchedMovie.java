package com.example.movieappbackend.domain.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
@Entity
public class WatchedMovie {

	@EmbeddedId
	@EqualsAndHashCode.Include
	private UserMoviePair userMoviePair;
	
	private float rate;
}
