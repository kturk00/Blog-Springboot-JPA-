package com.jeproject3.proj.controller.api;


import com.jeproject3.proj.domain.*;
import com.jeproject3.proj.service.*;
import com.jeproject3.proj.domain.Comment;
import com.jeproject3.proj.service.AuthorManager;
import com.jeproject3.proj.service.CommentManager;
import com.jeproject3.proj.service.PostAuthorsManager;
import com.jeproject3.proj.service.PostManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CommentController {
    @Autowired
    CommentManager cm;
    @Autowired
    AuthorManager am;
    @Autowired
    PostAuthorsManager pam;
    @Autowired
    PostManager pm;

    @GetMapping("/api/comment")
    public List<Comment> getComments() {
        return cm.getAllComments();
    }

    @GetMapping("/api/comment/user/{username}")
    public List<Comment> getCommentsOfUser(@PathVariable String username) {
        return cm.findByAuthor(username);
    }

    @PostMapping("/api/comment")
    Comment addComment(@RequestBody Comment comment) {
        Comment commentToAdd = new Comment(comment.getUsername(),comment.getLinkedPost(),comment.getCommentContent());
        cm.addComment(commentToAdd);
        return commentToAdd;
    }

    @PostMapping("/api/comment/post/{id}")
    String addCommentToPost(@RequestBody @Valid Comment comment, Errors errors, @PathVariable("id") long id ) {
        if(errors.hasErrors())
            return "Data not valid";
        Comment commentToAdd = new Comment(comment.getUsername(),comment.getLinkedPost(),comment.getCommentContent());
        commentToAdd.setLinkedPost(pm.findById(id));
        cm.addComment(commentToAdd);
        return commentToAdd.toString();
    }



    @GetMapping("/api/comment/{id}")
    Comment getComment(@PathVariable("id") long id) {
        return cm.findById(id);
    }

    @PutMapping("/api/comment/{id}")
    String replaceComment(@RequestBody @Valid Comment comment,Errors errors, @PathVariable("id") long id) {
        if(errors.hasErrors())
            return "Data invalid";
        cm.replace(id, comment);
        return cm.findById(id).toString();
    }

    @DeleteMapping("/api/comment/{id}")
    void deleteComment(@PathVariable("id") long id) {
        cm.remove(id);
    }
}
