package com.jeproject3.proj.service;

import com.jeproject3.proj.data.PostAuthor_csv;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


public interface PostAuthorsManager {
    void loadCSV() throws FileNotFoundException;

    void saveToCSV() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException;

    void addPostAuthor(PostAuthor_csv postAuthor);

    List<PostAuthor_csv> getAllPostAuthors();

    void remove(long idAuthor, long idPost);

    void removeByPost(long id);

    void removeByAuthor(long  id);


    List<PostAuthor_csv> findByAuthor(long idAuthor);

    List<PostAuthor_csv> findByPost(long idPost);

    List<Long> getPostsOfAuthor(long idAuthor);

    List<Long> getAuthorsOfPost(long idPost);
}

