package com.example.movieappbackend.domain.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class ResponseComment extends Comment implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@ManyToOne
    @JoinColumn(name = "id_post_comment", nullable = false)
    private PostComment postComment;
}
