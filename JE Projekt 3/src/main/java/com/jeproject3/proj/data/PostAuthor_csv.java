package com.jeproject3.proj.data;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostAuthor_csv {
    @NotBlank
    @CsvBindByName(column = "id_author", required = true)
    private long idAuthor;
    @NotBlank
    @CsvBindByName(column = "id_post", required = true)
    private long idPost;

    public PostAuthor_csv(long idAuthor, long idPost)
    {
        this.idAuthor = idAuthor;
        this.idPost = idPost;
    }

    public PostAuthor_csv(){}



}
