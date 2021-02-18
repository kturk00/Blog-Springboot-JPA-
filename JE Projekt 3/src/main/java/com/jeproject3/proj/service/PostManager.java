package com.jeproject3.proj.service;

import com.jeproject3.proj.domain.Post;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface PostManager {
    void loadCSV() throws FileNotFoundException;

    void saveToCSV() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException;

    void addPost(Post post);

    Post findById(long id);

    List<Post> getAllPosts();

    List<Post> getPostsWithIDs(List<Long> ids);

    void remove(long id);

    void editPost(long id, Post post);

    void replacePost(long id, Post post);

    List<Post> findByTag(String tag);

}
