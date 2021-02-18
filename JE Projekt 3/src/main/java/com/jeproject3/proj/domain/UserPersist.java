package com.jeproject3.proj.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;


@Entity
@Data
public class UserPersist {

    @Id @GeneratedValue
    private long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6,  message = "Password must have at least 6 characters")
    private String password;

    @NotBlank(message = "Passwords must match")
    private String matchingPassword;
    private String role;
    private boolean enabled;

    public UserPersist(String firstName, String lastName, String username, String password, String role, boolean enabled)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.matchingPassword = password;
        this.role = role;
        this.enabled = enabled;

    }

    public UserPersist(String firstName, String lastName, String username, String password, String matchingPassword)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.role = "USER";
        this.enabled = true;

    }

    public UserPersist(){};
}

