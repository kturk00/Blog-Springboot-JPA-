package com.jeproject3.proj;


import com.jeproject3.proj.domain.UserPersist;
import com.jeproject3.proj.service.*;
import com.jeproject3.proj.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.FileNotFoundException;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class ProjectApplication {

    private AuthorManager am;
    private PostManager pm;
    private CommentManager cm;
    private PostAuthorsManager pam;
    private UserManager um;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProjectApplication(PostManager pm, CommentManager cm, AuthorManager am, PostAuthorsManager pam, PasswordEncoder passwordEncoder, UserManager um) throws FileNotFoundException {
        this.am = am;
        this.pm = pm;
        this.cm = cm;
        this.pam = pam;
        this.um = um;
        this.passwordEncoder = passwordEncoder;

        am.loadCSV();
        pm.loadCSV();
        cm.loadCSV();
        pam.loadCSV();
        UserPersist admin = new UserPersist("Mariusz","Adminowski","admin",
                passwordEncoder.encode("admin123"),"ROLE_ADMIN",true);
        System.out.println(admin);
        um.addUser(admin);
    }
    public static void main(String[] args) {


        SpringApplication.run(ProjectApplication.class, args);

    }

}
