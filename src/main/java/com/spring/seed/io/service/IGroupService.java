package com.spring.seed.io.service;

import com.spring.seed.io.entity.Group;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface IGroupService {

    long count();

    Group create(Group resource);

    Page<Group> findAll();

    void delete(String id);

    Page<Group> findAllPaginatedAndSorted(int page, int size, String sortBy, String sortOrder);

    Page<Group> search(int page, int size, String sortBy, String sortOrder, Map<String, String[]> filters);

    void update(String id, Group resource);

    Group findOne(String id);

    boolean login(Map<String, String[]> filters);
}

