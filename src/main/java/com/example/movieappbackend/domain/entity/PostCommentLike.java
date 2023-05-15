package com.example.movieappbackend.domain.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
@Data
@Entity
public class PostCommentLike implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private final String uuid = UUID.randomUUID().toString();
    
	@ManyToOne
    @JoinColumn(name = "id_post_comment")
    private PostComment postComment;
	
	@ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}
