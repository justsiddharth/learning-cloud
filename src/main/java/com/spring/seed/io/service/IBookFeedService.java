package com.spring.seed.io.service;

import com.spring.seed.io.entity.BookFeed;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface IBookFeedService {

    long count();

    BookFeed create(BookFeed resource);

    Page<BookFeed> findAll();

    void delete(String id);

    Page<BookFeed> findAllPaginatedAndSorted(int page, int size, String sortBy, String sortOrder);

    Page<BookFeed> search(int page, int size, String sortBy, String sortOrder, Map<String, String[]> filters);

    void update(String id, BookFeed resource);

    BookFeed findOne(String id);
}

