package com.jeproject3.proj.service;

import com.jeproject3.proj.domain.MyUserDetails;
import com.jeproject3.proj.domain.UserPersist;

import java.util.List;

public interface UserManager {
    void addUser(UserPersist user);

    List<MyUserDetails> getAllUsers();

    MyUserDetails findByUsername(String username);

    Boolean isUsernameTaken(String username);
}
