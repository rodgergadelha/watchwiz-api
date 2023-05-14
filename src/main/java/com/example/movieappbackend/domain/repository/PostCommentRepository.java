package com.example.movieappbackend.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.movieappbackend.domain.entity.PostComment;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long>{

	Optional<PostComment> findByUuid(String uuid);
}
