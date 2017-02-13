package com.spring.seed.io.entity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "prod_group", type = "group", shards = 1, replicas = 0)
public class Group {

    @Id
    private String id = UUID.randomUUID().toString();

    @NotNull
    private String name;

    private String organization;

    private int total_members;

    private Map<String, Object> data;

    private String createdDate;

    private String modifiedDate;
}

