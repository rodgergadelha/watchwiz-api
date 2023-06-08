package com.example.movieappbackend.domain.model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.OffsetDateTime;
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
    
	private final String uuid = UUID.randomUUID().toString();
	
	private String url;
    
	@Column(columnDefinition = "TEXT")
    private String text;
    
    private Float rate;
    
	private String movieImdbId;
	
	@CreationTimestamp
	@Column(columnDefinition = "timestamp")
	private OffsetDateTime creationDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;
}