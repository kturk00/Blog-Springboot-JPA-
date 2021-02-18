package com.jeproject3.proj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeproject3.proj.data.Author_csv;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.*;

@Entity
public class Author {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String username;
    @JsonIgnoreProperties("authorsLinked")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH},fetch = FetchType.EAGER)
    private List<Post> postsLinked = new ArrayList<>();

    public Author(long id ,String firstName, String lastName, String username){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public Author(String firstName, String lastName, String username){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public Author(Author_csv ac)
    {
        this.id = Long.parseLong(ac.getId());
        this.firstName = ac.getFirstName();
        this.lastName = ac.getLastName();
        this.username = ac.getUsername();
    }

    public Author(){
    }

    public void addPost(Post post)
    {
        post.getAuthorsLinked().add(this);
        postsLinked.add(post);
    }

    public void removePost(Post post)
    {
        post.getAuthorsLinked().remove(this);
        postsLinked.remove(post);
    }

    public void removePostNoLink(Post post)
    {
        postsLinked.remove(post);
    }

    public void removeAllPosts()
    {
        for(Post pos : postsLinked)
            pos.removeAuthorNoLink(this);
        postsLinked.clear();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Post> getPostsLinked() {
        return postsLinked;
    }

    public void setPostsLinked(List<Post> postsLinked) {
        this.postsLinked = postsLinked;
    }
}
