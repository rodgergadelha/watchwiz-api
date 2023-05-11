package com.example.movieappbackend.domain.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
@Data
@Entity
public class CommentLike implements Serializable{
    private static final long serialVersionUID = 1L;
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private final String uuid = UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name = "id_comment", nullable = false)
    private Post post;
}
