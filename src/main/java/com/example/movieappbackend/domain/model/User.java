package com.example.movieappbackend.domain.model;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
@Entity
@Table(name = "app_user")
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
    private LocalDate birthdate;
    
    private String password;
    
    private boolean enabled;
    
    @CreationTimestamp
	@Column(columnDefinition = "timestamp")
	private OffsetDateTime registrationDate;
    
    @ManyToMany
    @JoinTable(name = "favorites",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "movie_list_item_imdb_id"))
    private List<MovieListItem> favorites;
    
    @ManyToMany
    @JoinTable(name = "watch_later",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "movie_list_item_imdb_id"))
    private List<MovieListItem> watchLater;
    
    @ManyToMany
    @JoinTable(name = "following",
    joinColumns = @JoinColumn(name = "follower_user_id"),
    inverseJoinColumns = @JoinColumn(name = "followed_user_id"))
    private List<User> following;
}