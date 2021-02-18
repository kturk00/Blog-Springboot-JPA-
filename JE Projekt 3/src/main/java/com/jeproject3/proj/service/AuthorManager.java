package com.jeproject3.proj.service;

import com.jeproject3.proj.domain.Author;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface AuthorManager {
    void loadCSV() throws FileNotFoundException;

    void saveToCSV() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException;

    void addAuthor(Author author);

    Author findById(long id);

    List<Author> getAllAuthors();

    List<Author> getAuthorsWithIDs(List<Long> ids);

    void remove(long id);

    void replace(long id, Author author);

    List<Author> findByUsername(String username);
}
