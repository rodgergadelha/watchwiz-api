package com.example.movieappbackend.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.movieappbackend.domain.model.Post;
import com.example.movieappbackend.domain.model.PostComment;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long>{

	Optional<PostComment> findByUuid(String uuid);
	
	void deleteByUuid(String uuid);
	
	Page<PostComment> findAllByPost(Post post, Pageable pageable);
	
	@Query("select count(*) from PostCommentLike where postComment = :postComment")
	int getTotalLikesFromPostComment(@Param("postComment") PostComment postComment);
}
