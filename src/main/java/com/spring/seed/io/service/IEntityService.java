package com.spring.seed.io.service;

import com.spring.seed.io.entity.User;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface IEntityService {

    long count();

    User create(User resource);

    Page<User> findAll();

    void delete(String id);

    Page<User> findAllPaginatedAndSorted(int page, int size, String sortBy, String sortOrder);

    Page<User> search(int page, int size, String sortBy, String sortOrder, Map<String, String[]> filters);

    void update(String id, User resource);

    User findOne(String id);
}

