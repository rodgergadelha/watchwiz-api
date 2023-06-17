package com.example.movieappbackend.domain.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class PostComment extends Comment implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_post")
    private Post post;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "post_comment_like",
    joinColumns = @JoinColumn(name = "post_comment_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> usersThatLiked;
}
