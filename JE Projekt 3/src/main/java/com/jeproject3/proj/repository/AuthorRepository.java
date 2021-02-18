package com.jeproject3.proj.repository;

import com.jeproject3.proj.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findById(long id);
    List<Author> findByFirstName(String firstName);
    List<Author> findByLastName(String lastName);
    List<Author> findByUsername(String username);
}
