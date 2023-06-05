package com.example.movieappbackend.domain.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
@Entity
public class WatchLater implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private UserMoviePair id;
}
