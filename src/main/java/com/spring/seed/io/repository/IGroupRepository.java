package com.spring.seed.io.repository;

import com.spring.seed.io.entity.Group;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IGroupRepository extends ElasticsearchRepository<Group, String> {
    //void update(String id, Group resource);
}


