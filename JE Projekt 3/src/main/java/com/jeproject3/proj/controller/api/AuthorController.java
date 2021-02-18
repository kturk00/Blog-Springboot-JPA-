package com.jeproject3.proj.controller.api;

import com.jeproject3.proj.domain.Author;
import com.jeproject3.proj.service.AuthorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthorController {
    @Autowired
    AuthorManager am;

    @GetMapping("/api/author")
    public List<Author> getAuthors() {
        return am.getAllAuthors();
    }

    @PostMapping("/api/author")
    Author addAuthor(@RequestBody Author author) {
        Author authorToAdd = new Author(author.getFirstName(),author.getLastName(),author.getUsername());
        am.addAuthor(authorToAdd);
        return authorToAdd;
    }

    @GetMapping("/api/author/{id}")
    Author getAuthor(@PathVariable("id") long id) {
        return am.findById(id);
    }

    @PutMapping("/api/author/{id}")
    Author replaceAuthor(@RequestBody Author author, @PathVariable("id") long id) {
        am.replace(id, author);
        return am.findById(author.getId());
    }

    @DeleteMapping("/api/author/{id}")
    void deleteAuthor(@PathVariable("id") long id) {
        am.remove(id);
    }
}
