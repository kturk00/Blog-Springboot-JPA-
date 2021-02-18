package com.jeproject3.proj.repository;

import com.jeproject3.proj.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post findById(long id);
    List<Post> findByTags(String tag);
}
