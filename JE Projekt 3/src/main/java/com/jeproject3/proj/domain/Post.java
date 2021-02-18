package com.jeproject3.proj.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeproject3.proj.data.Post_csv;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.test.annotation.Rollback;

import javax.persistence.*;

import java.util.*;

@Entity
public class Post {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "There must be some text")
    @Column(length = 1000)
    private String postContent;
    @ElementCollection
    private List<String> tags = new ArrayList<>();
    @JsonIgnoreProperties("postsLinked")
    @ManyToMany(mappedBy = "postsLinked", fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Author> authorsLinked = new ArrayList<>();
    @OneToMany(mappedBy = "linkedPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentsLinked = new ArrayList<>();

    public Post(String postContent, List<String> tags)
    {
        this.postContent = postContent;
        this.tags = tags;
    }

    public Post(String postContent)
    {
        this.postContent = postContent;
    }

    public Post(Post_csv pc)
    {
        this.id = Long.parseLong(pc.getId());
        this.postContent = pc.getPostContent();
        this.tags = pc.getTags();
    }

    public Post()
    {
        tags = new ArrayList<>();
    }

    public void addAuthor(Author author)
    {
        author.getPostsLinked().add(this);
        authorsLinked.add(author);
    }

    public void removeAuthor(Author author)
    {
        author.getPostsLinked().remove(this);
        authorsLinked.remove(author);
    }

    public void removeAuthorNoLink(Author author)
    {
        author.getPostsLinked().remove(this);
        authorsLinked.remove(author);
    }

    public void removeAllAuthors()
    {
        for(Author aut : authorsLinked)
            aut.removePostNoLink(this);
        authorsLinked.clear();
    }

    public void addTag(String tag)
    {
        System.out.println("***************  " + tag + " ***************** " + tags);
        tags.add(tag);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Author> getAuthorsLinked() {
        return authorsLinked;
    }

    public void setAuthorsLinked(List<Author> authorsLin) {
        removeAllAuthors();
        for(Author aut : authorsLin)
            addAuthor(aut);
    }

    public List<Comment> getCommentsLinked() {

        return commentsLinked;
    }

    public void setCommentsLinked(List<Comment> commentsLink) {
        commentsLinked.clear();
        commentsLinked.addAll(commentsLink);
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
}
