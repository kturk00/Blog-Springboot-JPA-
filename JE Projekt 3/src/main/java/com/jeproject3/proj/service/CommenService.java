package com.jeproject3.proj.service;

import com.jeproject3.proj.data.Comment_csv;
import com.jeproject3.proj.domain.Comment;
import com.jeproject3.proj.repository.CommentRepository;
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
public class CommenService implements CommentManager {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;


    @Override
    public void loadCSV() throws FileNotFoundException {
        List<Comment_csv> comments = new CsvToBeanBuilder<Comment_csv>(new FileReader("data-files/Comments2.csv"))
                .withType(Comment_csv.class).build().parse();
        List<Comment> toSave = new ArrayList<>();
        for(Comment_csv com : comments)
            toSave.add(new Comment(com, postRepository.findById(Long.parseLong(com.getIdPost()))));
        commentRepository.saveAll(toSave);
    }

    @Override
    public void saveToCSV() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        Writer writer = Files.newBufferedWriter(Paths.get("data-files/Comments2.csv"));

        StatefulBeanToCsv<Comment> beanToCsv = new StatefulBeanToCsvBuilder<Comment>(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();
        beanToCsv.write(commentRepository.findAll());
        writer.close();
    }

    @Override
    public void addComment(Comment comment) {
        if(findById(comment.getId()) == null)
            commentRepository.save(comment);
        else
            replace(comment.getId(),comment);

    }

    @Override
    public Comment findById(long id) {
        for (Comment comment : commentRepository.findAll()) {
            if (comment.getId() == id) {
                return comment;
            }
        }
        return null;
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public void remove(long id) {
        Comment toRemove = null;
        for (Comment comment : commentRepository.findAll()) {
            if (comment.getId() == id) {
                toRemove = comment;
                break;
            }
        }
        if (toRemove != null) {
            commentRepository.delete(toRemove);
        }
    }

    @Override
    public void replace(long id, Comment comment) {
        Comment com = commentRepository.findById(id);
        com.setCommentContent(comment.getCommentContent());
        com.setUsername(comment.getUsername());
        commentRepository.save(com);

    }

  /*  @Override
    public List<Comment> findByPost(long id) {
        List<Comment> result = new ArrayList<>();
        for (Comment comment : commentRepository.findAll())
            if (comment.getIdPost() == id)
                result.add(comment);
        return result;
    } */

    @Override
    public List<Comment> findByAuthor(String username) {
        List<Comment> result = new ArrayList<>();
        for (Comment comment : commentRepository.findAll())
            if (comment.getUsername().equals(username))
                result.add(comment);
        return result;
    }
}
