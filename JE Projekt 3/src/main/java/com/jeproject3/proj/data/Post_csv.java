package com.jeproject3.proj.data;
import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Post_csv {
    @NotBlank
    @CsvBindByName(required = true)
    private String id;
    @NotBlank(message = "There must be some text")
    @CsvBindByName(column = "post_content", required = true)
    private String postContent;
    @CsvBindAndSplitByName(elementType = String.class)
    private List<String> tags;

    public Post_csv(String postContent, List<String> tags)
    {
        this.postContent = postContent;
        id = UUID.randomUUID().toString();
        this.tags = tags;
    }
    public Post_csv(String postContent)
    {
        this.postContent = postContent;
        id = UUID.randomUUID().toString();
        tags = new ArrayList<>();
    }

    public Post_csv()
    {
        tags = new ArrayList<>();
    }

    public void addTag(String tag)
    {
        tags.add(tag);
    }
}
