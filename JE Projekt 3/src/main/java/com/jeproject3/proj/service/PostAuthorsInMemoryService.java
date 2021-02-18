package com.jeproject3.proj.service;

import com.jeproject3.proj.data.PostAuthor_csv;
import com.jeproject3.proj.domain.Author;
import com.jeproject3.proj.domain.Post;
import com.jeproject3.proj.repository.AuthorRepository;
import com.jeproject3.proj.repository.PostRepository;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostAuthorsInMemoryService implements PostAuthorsManager {
    private static List<PostAuthor_csv> postAuthors = new ArrayList<>();
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AuthorRepository authorRepository;


    @Override
    public void loadCSV() throws FileNotFoundException {
        postAuthors = new CsvToBeanBuilder<PostAuthor_csv>(new FileReader("data-files/Posts_Authors.csv"))
                .withType(PostAuthor_csv.class).build().parse();
        Author author;
        Post post;
        for(PostAuthor_csv pa : postAuthors)
        {
            author = authorRepository.findById(pa.getIdAuthor());
            post = postRepository.findById(pa.getIdPost());
            author.addPost(post);
            authorRepository.save(author);
            postRepository.save(post);
        }
    }

    @Override
    public void saveToCSV() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        Writer writer = Files.newBufferedWriter(Paths.get("data-files/Posts_Authors.csv"));

        StatefulBeanToCsv<PostAuthor_csv> beanToCsv = new StatefulBeanToCsvBuilder<PostAuthor_csv>(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();
        beanToCsv.write(postAuthors);
        writer.close();
    }

    @Override
    public void addPostAuthor(PostAuthor_csv postAuthor) {
        postAuthors.add(postAuthor);
    }


    @Override
    public List<PostAuthor_csv> getAllPostAuthors() {
        return postAuthors;
    }

    @Override
    public void remove(long idAuthor, long idPost) {
        PostAuthor_csv toRemove = null;
        for (PostAuthor_csv postAuthor : postAuthors) {
            if (postAuthor.getIdAuthor() == idAuthor && postAuthor.getIdPost() == idPost) {
                toRemove = postAuthor;
                break;
            }
        }
        if (toRemove != null) {
            postAuthors.remove(toRemove);
        }
    }

    @Override
    public void removeByPost(long id) {
        postAuthors.removeIf(x -> x.getIdPost() == id);
    }

    @Override
    public void removeByAuthor(long id) {
        postAuthors.removeIf(x -> x.getIdAuthor() == id);
    }


    @Override
    public List<PostAuthor_csv> findByAuthor(long idAuthor) {
        List<PostAuthor_csv> result = new ArrayList<>();
        for (PostAuthor_csv postAuthor : postAuthors)
            if (postAuthor.getIdAuthor() == idAuthor)
                result.add(postAuthor);
        return result;
    }

    @Override
    public List<PostAuthor_csv> findByPost(long idPost) {
        List<PostAuthor_csv> result = new ArrayList<>();
        for (PostAuthor_csv postAuthor : postAuthors)
            if (postAuthor.getIdPost() == idPost)
                result.add(postAuthor);
        return result;
    }

    @Override
    public List<Long> getPostsOfAuthor(long idAuthor) {
        List<Long> result = new ArrayList<>();
        for (PostAuthor_csv postAuthor : postAuthors)
            if (postAuthor.getIdAuthor() == idAuthor)
                result.add(postAuthor.getIdPost());
        return result;
    }

    @Override
    public List<Long> getAuthorsOfPost(long idPost) {
        List<Long> result = new ArrayList<>();
        for (PostAuthor_csv postAuthor : postAuthors)
            if (postAuthor.getIdPost() == idPost)
                result.add(postAuthor.getIdAuthor());
        return result;
    }
}
