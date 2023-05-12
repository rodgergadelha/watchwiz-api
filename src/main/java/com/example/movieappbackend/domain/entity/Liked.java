package com.example.movieappbackend.domain.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
@Entity
public class Liked implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "id_user")
	@EqualsAndHashCode.Include
    @Id
    private User user;
	
	@EqualsAndHashCode.Include
    @Id
	private String movieImdbId;
}
