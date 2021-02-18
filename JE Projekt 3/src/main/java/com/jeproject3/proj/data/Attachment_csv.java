package com.jeproject3.proj.data;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class Attachment_csv {
    @CsvBindByName(column = "id_post", required = true)
    private String idPost;
    @CsvBindByName(required = true)
    private String filename;


}
