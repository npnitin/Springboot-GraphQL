package com.example.springbootgraphql.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Book {

    @Id
    private String isbn;
    private String title;
    private String publishe;
    private String[] authors;
    private String publishedDate;

    public Book(String isbn, String title, String publishe, String[] authors, String publishedDate) {
        this.isbn = isbn;
        this.title = title;
        this.publishe = publishe;
        this.authors = authors;
        this.publishedDate = publishedDate;
    }
}
