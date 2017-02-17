package com.spring.seed.io.entity;

import com.spring.seed.io.dto.StatusEnum;
import java.util.Map;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "prod_bookfeed", type = "bookfeed", shards = 1, replicas = 0)
public class BookFeed {

    @Id
    private String id = UUID.randomUUID().toString();

    @NotNull
    private String title;

    @NotNull
    private String authorName;

    private String userImgUrl;

    private String imageUrl;

    private String userUUID;

    private String userName;

    private String name;

    private String bookPoints;

    private String groupIds;

    private StatusEnum status;

    private Map<String, Object> data;

    private String createdDate;

    private String modifiedDate;

    private boolean isDeprecated;

    private String recommendedUserId;

    private String recommendedUserName;

    private String recommendedUserIonicId;
}


