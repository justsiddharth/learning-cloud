package com.spring.seed.io.entity;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "entityindex", type = "entityname", shards = 1, replicas = 0)
public class Entity {

    @Id
    private String id;

    @NotNull
    private String name;

    Map<String, Object> data;

}

