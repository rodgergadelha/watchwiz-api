package com.example.movieappbackend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.movieappbackend.domain.entity.ResponseComment;

@Repository
public interface ResponseCommentRepository extends JpaRepository<ResponseComment, Long>{

}
