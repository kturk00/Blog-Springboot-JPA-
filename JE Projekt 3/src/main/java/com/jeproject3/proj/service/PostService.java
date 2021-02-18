package com.jeproject3.proj.service;
import com.jeproject3.proj.data.Post_csv;
import com.jeproject3.proj.domain.Post;
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
public class PostService implements PostManager {
    @Autowired
    private PostRepository postRepository;

    public void loadCSV() throws FileNotFoundException {
        List<Post_csv> posts = new CsvToBeanBuilder<Post_csv>(new FileReader("data-files/Posts.csv"))
                .withType(Post_csv.class).build().parse();
        List<Post> toSave = new ArrayList<>();
        for(Post_csv pos : posts)
            toSave.add(new Post(pos));
        postRepository.saveAll(toSave);
    }

    @Override
    public void saveToCSV() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        Writer writer = Files.newBufferedWriter(Paths.get("data-files/Posts.csv"));

        StatefulBeanToCsv<Post> beanToCsv = new StatefulBeanToCsvBuilder<Post>(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();
        beanToCsv.write(postRepository.findAll());
        writer.close();
    }

    public void addPost(Post post) {
        postRepository.save(post);

    }

    @Override
    public Post findById(long id) {
        for (Post post : postRepository.findAll()) {
            if (post.getId() == id) {
                return post;
            }
        }
        return null;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getPostsWithIDs(List<Long> ids) {
        List<Post> result = new ArrayList<>();
        for (long id : ids)
            result.add(findById(id));
        return result;
    }

    @Override
    public void remove(long id) {

        Post toRemove = null;
        for (Post post : postRepository.findAll()) {
            if (post.getId() == id) {
                toRemove = post;
                break;
            }
        }
        if (toRemove != null) {
            postRepository.delete(toRemove);
        }
    }

    @Override
    public void editPost(long id, Post post) {
        Post toEdit = postRepository.findById(id);
        toEdit.setAuthorsLinked(post.getAuthorsLinked());
        toEdit.setPostContent(post.getPostContent());
        toEdit.setTags(post.getTags());
        postRepository.save(toEdit);

    }

    @Override
    public void replacePost(long id, Post post) {
        Post toEdit = postRepository.findById(id);
        toEdit.setAuthorsLinked(post.getAuthorsLinked());
        toEdit.setPostContent(post.getPostContent());
        toEdit.setTags(post.getTags());
        toEdit.setCommentsLinked(post.getCommentsLinked());
        postRepository.save(toEdit);

    }

    @Override
    public List<Post> findByTag(String tagToSearch) {
        List<Post> result = new ArrayList<>();
        for (Post post : postRepository.findAll())
            if (post.getTags().contains(tagToSearch))
                result.add(post);
        return result;
    }

}
