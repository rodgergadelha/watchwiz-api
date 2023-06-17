package com.example.movieappbackend.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.movieappbackend.domain.model.PostComment;
import com.example.movieappbackend.domain.model.ResponseComment;

@Repository
public interface ResponseCommentRepository extends JpaRepository<ResponseComment, Long>{
	
	Optional<ResponseComment> findByUuid(String uuid);
	
	void deleteByUuid(String uuid);
	
	Page<ResponseComment> findAllByPostComment(PostComment postComment, Pageable pageable);
}
