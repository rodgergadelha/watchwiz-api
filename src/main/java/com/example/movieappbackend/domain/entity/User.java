package com.example.movieappbackend.domain.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;
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
    
    private Date birthdate;
    
    private String password;
    
    @CreationTimestamp
	@Column(columnDefinition = "datetime")
	private OffsetDateTime registrationDate;
}