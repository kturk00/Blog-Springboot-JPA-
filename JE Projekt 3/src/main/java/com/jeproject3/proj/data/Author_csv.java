package com.jeproject3.proj.data;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class Author_csv {
    @CsvBindByName(required = true)
    private String id;
    @CsvBindByName(column = "first_name", required = true)
    private String firstName;
    @CsvBindByName(column = "last_name", required = true)
    private String lastName;
    @CsvBindByName(required = true)
    private String username;

    public Author_csv(String firstName, String lastName, String username){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public Author_csv(){
    }
}
