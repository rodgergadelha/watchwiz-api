package com.example.movieappbackend.domain.model;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
@Entity
public class Post implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	private final String uuid = UUID.randomUUID().toString();
    
	@Column(columnDefinition = "TEXT")
    private String text;
    
	@OneToOne
	@JoinColumns({@JoinColumn(name = "id_movie_list_item"), @JoinColumn(name = "id_user")})
	private WatchedMovie watchedMovie;
	
	@CreationTimestamp
	@Column(columnDefinition = "timestamp")
	private OffsetDateTime creationDate;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "post_like",
    joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> usersThatLiked;
}