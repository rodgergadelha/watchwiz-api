package com.example.movieappbackend.domain.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

@EqualsAndHashCode
@Data
@Entity
public class Post implements Serializable{
    private static final long serialVersionUID = 1L;
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private final String uuid = UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Float rate;
    @Column(nullable = false)
    private String imdb_id;
    @OneToMany(mappedBy = "post")
    private Collection<Comment> comments;
    //TODO p_filme

    public Collection<Comment> getComment() {
        return comments;
    }

    public void setComment(Collection<Comment> comment) {
        this.comments = comment;
    }
}