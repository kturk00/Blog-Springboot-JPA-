package com.jeproject3.proj.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeproject3.proj.data.Comment_csv;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
public class Comment {
    @NotBlank(message = "ID is required")
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Username is required")
    @Size(min = 2, max = 200, message = "Username must be between 2 and 20 characters")
    private String username;
    @JsonIgnoreProperties("commentsLinked")
    @ManyToOne(fetch = FetchType.EAGER)
    private Post linkedPost;
    @NotBlank(message = "Content is required")
    @Column(length = 1000)
    private String commentContent;

    public Comment(long id,String username,Post linkedPost, String commentContent)
    {
        this.id = id;
        this.username = username;
        this.linkedPost = linkedPost;
        this.commentContent = commentContent;
    }

    public Comment(String username,Post linkedPost, String commentContent)
    {
        this.username = username;
        this.linkedPost = linkedPost;
        this.commentContent = commentContent;
    }

    public Comment(String username, String commentContent)
    {
        this.username = username;
        this.commentContent = commentContent;
    }

    public Comment(Comment_csv cs, Post linkedPost)
    {
        this.id = Long.parseLong(cs.getId());
        this.username = cs.getUsername();
        this.linkedPost = linkedPost;
        this.commentContent = cs.getCommentContent();

    }

    public Comment()
    {
    }

    public void setLinkedPost(Post linkedPost) {
        this.linkedPost = linkedPost;
        linkedPost.getCommentsLinked().add(this);
    }
}
