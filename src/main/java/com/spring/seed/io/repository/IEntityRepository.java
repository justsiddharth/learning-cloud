package com.spring.seed.io.repository;

import com.spring.seed.io.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IEntityRepository extends ElasticsearchRepository<User, String> {
}
