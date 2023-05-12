package com.example.movieappbackend.domain.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class PostComment extends Comment implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@ManyToOne
    @JoinColumn(name = "id_post", nullable = false)
    private Post post;
	
	@OneToMany(mappedBy = "postComment")
    private Collection<ResponseComment> responseComments;
}
