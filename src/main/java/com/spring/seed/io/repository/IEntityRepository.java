package com.spring.seed.io.repository;

import com.spring.seed.io.entity.Entity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IEntityRepository extends ElasticsearchRepository<Entity, String> {
}
