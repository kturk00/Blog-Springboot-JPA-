package com.jeproject3.proj.security;


import com.jeproject3.proj.domain.MyUserDetails;
import com.jeproject3.proj.domain.UserPersist;
import com.jeproject3.proj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            UserPersist user = repository.getUserByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("Could not find user");
            }

            return new MyUserDetails(user);
    }
}
