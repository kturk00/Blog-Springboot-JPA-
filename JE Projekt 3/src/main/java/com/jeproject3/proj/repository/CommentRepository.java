package com.jeproject3.proj.repository;

import com.jeproject3.proj.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
   Comment findById(long id);
   Comment findByUsername(String username);
}
