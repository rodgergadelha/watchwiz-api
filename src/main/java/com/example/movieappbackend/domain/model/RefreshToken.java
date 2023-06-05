package com.example.movieappbackend.domain.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@Data
@Entity
public class RefreshToken implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@EqualsAndHashCode.Include
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	private String token;
    
	private OffsetDateTime createdDate;
}