package com.example.movieappbackend.domain.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

@EqualsAndHashCode
@Data
@Entity
public class Post implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Column(nullable = false)
    private final String uuid = UUID.randomUUID().toString();
    
	@Column(columnDefinition = "TEXT", nullable = false)
    private String text;
    
	@Column(nullable = false)
    private Float rate;
    
	@Column(name = "movie_imdb_id", nullable = false)
    private String movieImdbId;
	
	@ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
    
	@OneToMany(mappedBy = "post")
    private Collection<PostComment> comments;
}