package com.jeproject3.proj.controller.api;


import com.jeproject3.proj.domain.Author;
import com.jeproject3.proj.domain.Comment;
import com.jeproject3.proj.domain.Post;
import com.jeproject3.proj.service.AuthorManager;
import com.jeproject3.proj.service.PostAuthorsManager;
import com.jeproject3.proj.service.PostManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class PostController {
    @Autowired
    PostManager pm;
    @Autowired
    AuthorManager am;
    @Autowired
    PostAuthorsManager pam;

    @GetMapping("/api/post")
    public List<Post> getPosts() {

        return pm.getAllPosts().stream().sorted(Comparator.comparing(Post::getPostContent)).collect(Collectors.toList());
        //return pm.getAllPosts();
    }

    @GetMapping("/api/post/author/{username}")
    public List<Post> getPostsFromAuthor(@PathVariable String username) {
        Author author = am.findByUsername(username).get(0);
        return pm.getPostsWithIDs(pam.getPostsOfAuthor(author.getId()));
    }

    @PostMapping("/api/post")
    Post addPost(@RequestBody Post post) {
        Post postToAdd = new Post(post.getPostContent(),post.getTags());
        pm.addPost(postToAdd);
        return postToAdd;
    }

    @GetMapping("/api/post/{id}")
    Post getPost(@PathVariable("id") long id) {
        return pm.findById(id);
    }

    @PutMapping("/api/post/{id}")
    Post replacePost(@RequestBody Post post, @PathVariable("id") long id) {
        pm.replacePost(id, post);
        return pm.findById(post.getId());
    }

    @DeleteMapping("/api/post/{id}")
    void deletePost(@PathVariable("id") long id) {
        pm.remove(id);
    }
}
