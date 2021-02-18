package com.jeproject3.proj.data;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class Comment_csv {
    @NotBlank(message = "ID is required")
    @CsvBindByName(required = true)
    private String id;
    @NotBlank(message = "Username is required")
    @CsvBindByName(required = true)
    private String username;
    @NotBlank(message = "IDPost is required")
    @CsvBindByName(column = "id_post", required = true)
    private String idPost;
    @NotBlank(message = "Content is required")
    @CsvBindByName(column = "comment_content", required = true)
    private String commentContent;

    public Comment_csv(String username, String idPost, String commentContent)
    {
        id = UUID.randomUUID().toString();
        this.username = username;
        this.idPost = idPost;
        this.commentContent = commentContent;
    }

    public Comment_csv()
    {
    }
}
