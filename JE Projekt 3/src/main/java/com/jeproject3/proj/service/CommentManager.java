package com.jeproject3.proj.service;

import com.jeproject3.proj.domain.Comment;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface CommentManager {
    void loadCSV() throws FileNotFoundException;

    void saveToCSV() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException;

    void addComment(Comment comment);

    Comment findById(long id);

    List<Comment> getAllComments();

    void remove(long id);

    void replace(long id, Comment comment);

   // List<Comment> findByPost(long id);

    List<Comment> findByAuthor(String username);
}
