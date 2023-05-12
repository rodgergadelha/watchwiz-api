package com.example.movieappbackend.domain.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
@MappedSuperclass
public abstract class Comment {
	
	@EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Column(nullable = false)
    private final String uuid = UUID.randomUUID().toString();
	
	@Column(columnDefinition = "TEXT", nullable = false)
    private String text;
    
	@ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
}
