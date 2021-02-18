package com.jeproject3.proj.controller.api;


import com.jeproject3.proj.domain.MyUserDetails;
import com.jeproject3.proj.domain.Post;
import com.jeproject3.proj.domain.UserPersist;
import com.jeproject3.proj.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private final UserManager userManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController( UserManager userManager, PasswordEncoder passwordEncoder) {
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;

    }

    @GetMapping("/api/users")
    public List<MyUserDetails> getUsers() {
        return userManager.getAllUsers();
    }

    @PostMapping("/api/register")
    public String register(@RequestBody @Valid UserPersist user, Errors errors) {
        if(errors.hasErrors() ){
            return "Data not valid. Password must be at least 6 characters long";
        }
        else if(!user.getPassword().equals(user.getMatchingPassword()))
            return "Passwords must be matching!";
        else if(userManager.isUsernameTaken(user.getUsername()))
            return "This username is already taken";
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setMatchingPassword(passwordEncoder.encode(user.getMatchingPassword()));
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        userManager.addUser(user);
        return userManager.findByUsername(user.getUsername()).toString();
    }
}
