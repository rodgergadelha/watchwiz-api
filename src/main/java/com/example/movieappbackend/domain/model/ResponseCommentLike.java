package com.example.movieappbackend.domain.model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
@Data
@Entity
public class ResponseCommentLike implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private final String uuid = UUID.randomUUID().toString();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_response_comment")
    private ResponseComment responseComment;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;
}