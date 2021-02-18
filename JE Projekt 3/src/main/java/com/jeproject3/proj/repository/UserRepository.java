package com.jeproject3.proj.repository;

import com.jeproject3.proj.domain.UserPersist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserPersist, Long> {
    UserPersist getUserByUsername(String username);
}
