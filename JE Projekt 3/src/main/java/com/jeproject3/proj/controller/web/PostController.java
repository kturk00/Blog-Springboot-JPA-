package com.jeproject3.proj.controller.web;

import com.jeproject3.proj.data.PostAuthor_csv;
import com.jeproject3.proj.domain.*;
import com.jeproject3.proj.service.*;

import com.jeproject3.proj.domain.Author;
import com.jeproject3.proj.domain.Post;
import com.jeproject3.proj.service.AuthorManager;
import com.jeproject3.proj.service.CommentManager;
import com.jeproject3.proj.service.PostAuthorsManager;
import com.jeproject3.proj.service.PostManager;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Controller("postwebcontroller")
public class PostController {
    private PostManager pm;
    private CommentManager cm;
    private AuthorManager am;
    private PostAuthorsManager pam;

    @Autowired
    public PostController(PostManager pm, CommentManager cm, AuthorManager am , PostAuthorsManager pam) throws FileNotFoundException {
        this.pm = pm;
        this.cm = cm;
        this.am = am;
        this.pam = pam;
    }

    @GetMapping("/post")
    public String showPosts(Model model) {
        model.addAttribute("posts", pm.getAllPosts());
        return "posts";
    }

    @GetMapping("/post/{id}")
    public String showComments(@PathVariable("id") long id, Model model){
        List comments = pm.findById(id).getCommentsLinked().stream().sorted(Comparator.comparing(Comment::getUsername)).
                collect(Collectors.toList());
        model.addAttribute("post", pm.findById(id));
        model.addAttribute("comments", comments);
        model.addAttribute("authors", pm.findById(id).getAuthorsLinked());
        System.out.println(pm.findById(id).getTags());
        return "postComments";
    }

    @GetMapping("/post/author/{id}")
    public String showPostsFromAuthor(@PathVariable("id") long id, Model model){
        model.addAttribute("posts", am.findById(id).getPostsLinked());
        return "posts";
    }

    @GetMapping("/post/author/username/{username}")
    public String showPostsFromUsername(@PathVariable("username") String username, Model model){
        List<Author> posts = am.findByUsername(username);
        if(posts.isEmpty())
            model.addAttribute("posts", new ArrayList<Post>());
        else
        model.addAttribute("posts", posts.get(0).getPostsLinked());
        return "posts";
    }

    @GetMapping("/post/tag/{id}")
    public String showPostsFromTag(@PathVariable("id") String id, Model model){
        model.addAttribute("posts", pm.findByTag(id));
        return "posts";
    }

    @GetMapping("/post/create")
    public String postForm(Model model){
        Post post = new Post();
        model.addAttribute("post", post);
        model.addAttribute("authors", "");
        model.addAttribute("tagsL", "");
        return "postForm";
    }

    @PostMapping("/post/create")
    public String processOrderPost(@ModelAttribute @Valid Post post, Errors errors, @RequestParam String authors,
                                   @RequestParam String tagsL, Model model)
    {
        if(errors.hasErrors()){
            model.addAttribute("authors", "");
            model.addAttribute("tagsL", "");
            return "postForm";
        }
        List<String> authorsList = new ArrayList<String>(Arrays.asList(authors.split(" ")));
        List<String> tagsList = new ArrayList<String>(Arrays.asList(tagsL.split(" ")));
        setAuthors(post, authorsList);
        for(String tag : tagsList)
        {
            System.out.println(tag);
            if(!post.getTags().contains(tag))
                 post.addTag(tag);
        }
        pm.addPost(post);
        log.info("post created: " + post +  "    " + post.getTags());
        return "redirect:/post";
    }

    @RequestMapping("/post/{idP}/delete")
    public String deletePost(@PathVariable("idP") long idP)  {
        pm.findById(idP).removeAllAuthors();
        pm.remove(idP);
        pam.removeByPost(idP);
        return "redirect:/post";
    }

    @GetMapping("/post/{idP}/edit")
    public String postEditForm(@PathVariable("idP") long idP, Model model){
        Post post = pm.findById(idP);

        model.addAttribute("post", post);
        model.addAttribute("authors", post.getAuthorsLinked().stream().map(Author::getUsername).collect(Collectors.joining(" ")));
        model.addAttribute("tagsL",  String.join(" ", post.getTags()));
        return "postEdit";
    }

    @PostMapping("/post/{idP}/edit")
    public String processEditPost(@ModelAttribute @Valid Post post, Errors errors, @RequestParam String authors,
                                  @RequestParam String tagsL,@PathVariable("idP") long idP, Model model){
            if(errors.hasErrors()){
                model.addAttribute("authors", "");
                model.addAttribute("tagsL", "");
                return "postEdit";
            }
            Post toEdit = pm.findById(idP);
            List<String> authorsList = new ArrayList<String>(Arrays.asList(authors.split(" ")));
            List<String> tagsList = new ArrayList<String>(Arrays.asList(tagsL.split(" ")));
            toEdit.removeAllAuthors();
            setAuthors(toEdit, authorsList);
            toEdit.setTags(new ArrayList<>());
            for(String tag : tagsList)
            {
                System.out.println(tag);
                if(!toEdit.getTags().contains(tag))
                    toEdit.addTag(tag);
            }
            toEdit.setPostContent(post.getPostContent());
            pm.addPost(toEdit);
            log.info("post edited: " + post +  "    " + post.getTags());
            return "redirect:/post";
        }

    @GetMapping("/post/stats")
    public String showStats(Model model){
        List<Author> allAuthors = am.getAllAuthors();
        List<Post> allPosts = pm.getAllPosts();
        model.addAttribute("usernamesStartA", allAuthors.stream().map(Author::getUsername).filter(u -> u.toUpperCase().startsWith("A")).count());
        model.addAttribute("lettersInPosts", allPosts.stream().map(p -> p.getPostContent().length()).reduce(0, Integer::sum));
        model.addAttribute("longestUserName", allAuthors.stream()
                .map(Author::getUsername)
                .max(Comparator.comparingInt(String::length)).get());
        String shortestFirstName = allAuthors.stream()
                .map(Author::getFirstName)
                .min(Comparator.comparingInt(String::length)).get();
        model.addAttribute("shortFirstName", shortestFirstName);
        model.addAttribute("uniqueTags", allPosts.stream().map(Post::getTags).flatMap(Collection::stream).distinct().count());
        return "stats";
    }

    private void setAuthors(Post toEdit, List<String> authorsList) {
        Author newAut;
        for(String author : authorsList)
        {
            if(am.findByUsername(author).isEmpty()) {
                newAut = new Author("unknown", "unknown", author);
                am.addAuthor(newAut);
                log.info("author created: " + newAut);
            }
            toEdit.addAuthor(am.findByUsername(author).get(0));
        }
    }


}
