package com.spring.seed.io.service;

import com.spring.seed.io.entity.Entity;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface IEntityService {

    long count();

    Entity create(Entity resource);

    Page<Entity> findAll();

    void delete(String id);

    Page<Entity> findAllPaginatedAndSorted(int page, int size, String sortBy, String sortOrder);

    Page<Entity> search(int page, int size, String sortBy, String sortOrder, Map<String, String[]> filters);

    void update(String id, Entity resource);

    Entity findOne(String id);
}

