package com.spring.seed.io.entity;

import java.util.Map;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "prod_book", type = "book", shards = 1, replicas = 0)
public class Book {

    @Id
    private String id = UUID.randomUUID().toString();

    @NotNull
    private String title;

    @NotNull
    private String authorName;

    private int bookId;

    private double bookPoints;

    private String isbn;

    private String isbn13;

    private String publisher;

    private String description;

    private double averageRating;

    private String imageUrl;

    private String smallImageUrl;

    private String releaseDate;

    private Map<String, Object> data;

    private String createdDate;

    private String modifiedDate;

    private String num_pages;

    private String format;
}
