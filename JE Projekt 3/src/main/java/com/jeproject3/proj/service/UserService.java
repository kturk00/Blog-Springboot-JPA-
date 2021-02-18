package com.jeproject3.proj.service;

import com.jeproject3.proj.domain.MyUserDetails;
import com.jeproject3.proj.domain.UserPersist;
import com.jeproject3.proj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserManager {
    @Autowired
    private UserRepository repository;

    @Override
    public void addUser(UserPersist user) {
        repository.save(user);
    }

    @Override
    public List<MyUserDetails> getAllUsers() {
        List<MyUserDetails> usersDetails = new ArrayList<>();
        for(UserPersist u: repository.findAll())
            usersDetails.add(new MyUserDetails(u));
        return usersDetails;
    }

    @Override
    public MyUserDetails findByUsername(String username) {
        return new MyUserDetails(repository.getUserByUsername(username));
    }

    @Override
    public Boolean isUsernameTaken(String username) {
        return repository.getUserByUsername(username) != null;
    }
}
