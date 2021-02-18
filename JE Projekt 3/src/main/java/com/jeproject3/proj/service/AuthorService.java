package com.jeproject3.proj.service;

import com.jeproject3.proj.data.Author_csv;
import com.jeproject3.proj.domain.Author;
import com.jeproject3.proj.repository.AuthorRepository;
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
public class AuthorService implements AuthorManager{
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public void loadCSV() throws FileNotFoundException {
        List<Author_csv> authors = new CsvToBeanBuilder<Author_csv>(new FileReader("data-files/Authors.csv"))
                .withType(Author_csv.class).build().parse();
        List<Author> toSave = new ArrayList<>();
        for(Author_csv aut : authors)
            toSave.add(new Author(aut));
        authorRepository.saveAll(toSave);
    }

    @Override
    public void saveToCSV() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        Writer writer = Files.newBufferedWriter(Paths.get("data-files/Authors.csv"));

        StatefulBeanToCsv<Author> beanToCsv = new StatefulBeanToCsvBuilder<Author>(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();
        beanToCsv.write(authorRepository.findAll());
        writer.close();
    }

    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    @Override
    public Author findById(long id) {
        for (Author author : authorRepository.findAll()) {
            if (author.getId() == id) {
                return author;
            }
        }
        return null;
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author> getAuthorsWithIDs(List<Long> ids) {
        List<Author> result = new ArrayList<>();
        for (long id : ids)
            result.add(findById(id));
        return result;
    }

    @Override
    public void remove(long id) {

        Author toRemove = null;
        for (Author author : authorRepository.findAll()) {
            if (author.getId() == id) {
                toRemove = author;
                break;
            }
        }
        if (toRemove != null) {
            authorRepository.delete(toRemove);
        }
    }

    @Override
    public void replace(long id, Author author) {
        for (Author aut : authorRepository.findAll()) {
            if (aut.getId() == id) {
                aut.setFirstName(author.getFirstName());
                aut.setLastName(author.getLastName());
                aut.setUsername(author.getUsername());
                authorRepository.save(aut);
                break;
            }
        }
    }

    @Override
    public List<Author> findByUsername(String username ) {
        List<Author> result = new ArrayList<>();
        for (Author author : authorRepository.findAll())
            if (author.getUsername().equals(username))
                result.add(author);
        return result;
    }
}
