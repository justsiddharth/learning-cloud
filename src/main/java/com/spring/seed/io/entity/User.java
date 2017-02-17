package com.spring.seed.io.entity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "prod_user", type = "user", shards = 1, replicas = 0)
public class User {

    @Id
    private String id = UUID.randomUUID().toString();

    @NotNull
    private String name;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String organization;

    @NotNull
    private String emailId;

    private Set<String> categories;

    private Set<String> favorites;

    private Set<String> groups;

    private Long score;

    private String ionicId;

    private Map<String, Object> data;

    private String createdDate;

    private String modifiedDate;
}

