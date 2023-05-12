package com.example.movieappbackend.domain.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
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
    
    @Column(nullable = false)
    private final String uuid = UUID.randomUUID().toString();
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private Date birthdate;
    
    @Column(nullable = false)
    private String password;
}