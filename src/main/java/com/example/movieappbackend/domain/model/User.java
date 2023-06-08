package com.example.movieappbackend.domain.model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode
@Data
@Entity
public class User implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private final String uuid = UUID.randomUUID().toString();
    
    private String username;
    
    private String email;
    
    @Column(columnDefinition = "date")
    private Date birthdate;
    
    private String password;
    
    private boolean enabled;
    
    private String profileImagePath;
    
    @CreationTimestamp
	@Column(columnDefinition = "timestamp")
	private OffsetDateTime registrationDate;
    
    @ManyToMany
    @JoinTable(name = "watched",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "movie_list_item_imdb_id"))
    private List<MovieListItem> watched;
    
    @ManyToMany
    @JoinTable(name = "liked",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "movie_list_item_imdb_id"))
    private List<MovieListItem> liked;
    
    @ManyToMany
    @JoinTable(name = "watch_later",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "movie_list_item_imdb_id"))
    private List<MovieListItem> watchLater;
}