package com.spring.seed.io.entity;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "prod_book", type = "book", shards = 1, replicas = 0)
public class BookName {

    @Id
    private String id = UUID.randomUUID().toString();

    @NotNull
    private String title;

    @NotNull
    private int goodReadsId;
}

